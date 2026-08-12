#!/usr/bin/env bash

# Lokalne komande za pokretanje i proveru projekta.

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
ANDROID_DIR="$PROJECT_DIR/app"
BACKEND_DIR="$PROJECT_DIR/backend"
PACKAGE_NAME="com.stajkovicluka.financeapp"
PID_FILE="$PROJECT_DIR/.git/dev-backend.pid"
BACKEND_LOG="$BACKEND_DIR/logs/dev-backend-console.log"

if command -v adb >/dev/null 2>&1; then
    ADB="adb"
else
    ADB="$HOME/Library/Android/sdk/platform-tools/adb"
fi

show_help() {
    echo "-----------------------------"

    echo "HELPER SKRIPTA by Luka v1.5"
    echo "-----------------------------"
    echo "Koriscenje: ./dev.sh <komanda>"
    echo ""
    echo "Komande:"
    echo "  backend         Pokrece Spring Boot backend u pozadini"
    echo "  backend stop    Zaustavlja backend pokrenut ovom skriptom"
    echo "  backend-test    Pokrece backend testove"
    echo "  android-build   Pravi debug APK"
    echo "  android-test    Pokrece Android testove"
    echo "  android-install Gradi i instalira aplikaciju na telefon"
    echo "  android-run     Gradi, instalira i pokrece aplikaciju na telefonu"
    echo "  status          Prikazuje stanje backend-a i Android uredjaja"
}

backend_is_running() {
    if [ ! -f "$PID_FILE" ]; then
        return 1
    fi

    pid=$(cat "$PID_FILE")
    if ! [[ "$pid" =~ ^[0-9]+$ ]] || ! kill -0 "$pid" 2>/dev/null; then
        return 1
    fi

    process_command=$(ps -p "$pid" -o command= 2>/dev/null)
    [[ "$process_command" == *"$BACKEND_DIR"* && "$process_command" == *"bootRun"* ]]
}

remove_stale_pid_file() {
    if [ -f "$PID_FILE" ] && ! backend_is_running; then
        rm -f "$PID_FILE"
    fi
}

start_backend() {
    remove_stale_pid_file

    if backend_is_running; then
        echo "BACKEND je vec pokrenut ovom skriptom."
        return
    fi

    if curl --silent --fail --max-time 2 http://127.0.0.1:8080/health >/dev/null; then
        echo "BACKEND je vec pokrenut, ali ne preko dev.sh."
        return
    fi

    mkdir -p "$(dirname "$BACKEND_LOG")"
    (
        cd "$BACKEND_DIR" || exit 1
        exec ./gradlew bootRun --no-daemon
    ) > "$BACKEND_LOG" 2>&1 &

    echo $! > "$PID_FILE"
    echo "BACKEND se pokrece u pozadini."
    echo "Log: backend/logs/dev-backend-console.log"
}

stop_backend() {
    remove_stale_pid_file

    if ! backend_is_running; then
        echo "BACKEND NIJE pokrenut skriptom"
        return
    fi

    kill "$(cat "$PID_FILE")"
    rm -f "$PID_FILE"
    echo "BACKEND je zaustavljen"
}

require_device() {
    if [ ! -x "$ADB" ] && ! command -v adb >/dev/null 2>&1; then
        echo "ADB NIJE pronadjen. Povezi telefon kroz Android Studio ili dodaj ADB u PATH."
        exit 1
    fi

    if ! "$ADB" get-state 2>/dev/null | grep -q "device"; then
        echo "Android uredjaj NIJE povezan. Proveri Wi-Fi debugging"
        exit 1
    fi
}

case "$1" in
    backend)
        case "$2" in
            "") start_backend ;;
            stop) stop_backend ;;
            *) show_help ;;
        esac
        ;;
    backend-test)
        cd "$BACKEND_DIR" || exit 1
        ./gradlew test
        ;;
    android-build)
        cd "$ANDROID_DIR" || exit 1
        ./gradlew assembleDebug
        ;;
    android-test)
        cd "$ANDROID_DIR" || exit 1
        ./gradlew test
        ;;
    android-install)
        require_device
        cd "$ANDROID_DIR" || exit 1
        ./gradlew installDebug
        ;;
    android-run)
        require_device
        cd "$ANDROID_DIR" || exit 1
        ./gradlew installDebug && "$ADB" shell am start -n "$PACKAGE_NAME/.MainActivity"
        ;;
    status)
        remove_stale_pid_file
        echo "-> status projekta"
        echo "---------------"
        if curl --silent --fail --max-time 2 http://127.0.0.1:8080/health >/dev/null; then
            if backend_is_running; then
                echo "BACKEND: radi na http://localhost:8080 (dev.sh)"
            else
                echo "BACKEND: radi na http://localhost:8080 (pokrenut VAN dev.sh)"
            fi
        elif backend_is_running; then
            echo "BACKEND: pokrece se (dev.sh)"
        else
            echo "BACKEND: NIJE pokrenut"
        fi

        if [ -x "$ADB" ] || command -v adb >/dev/null 2>&1; then
            connected_device=$("$ADB" devices | awk 'NR > 1 && $2 == "device" { print $1; exit }')
            if [ -n "$connected_device" ]; then
                echo "ANDROID: povezan ($connected_device)"
            else
                echo "ANDROID: NIJE povezan"
            fi
        else
            echo "ANDROID: ADB NIJE pronadjen"
        fi
        ;;
    *)
        show_help
        ;;
esac
