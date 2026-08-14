package com.stajkovicluka.financeapp.data.quotes

// Motivacione poruke koje se prikazuju na pocetnom ekranu
object MotivationalQuotes {
    private val quotes = listOf(
        "Lorem Ipsum 1",
        "Lorem Ipsum 2",
        "Lorem Ipsum 3",
        "Lorem Ipsum 4",
        "Lorem Ipsum 5",
        "Lorem Ipsum 6",
        "Lorem Ipsum 7",
        "Lorem Ipsum 8",
        "Lorem Ipsum 9",
        "Lorem Ipsum 10",
        "Lorem Ipsum 11",
        "Lorem Ipsum 12",
        "Lorem Ipsum 13",
        "Lorem Ipsum 14",
        "Lorem Ipsum 15",
        "Lorem Ipsum 16",
        "Lorem Ipsum 17",
        "Lorem Ipsum 18",
        "Lorem Ipsum 19",
        "Lorem Ipsum 20",
        "Lorem Ipsum 21",
        "Lorem Ipsum 22",
        "Lorem Ipsum 23",
        "Lorem Ipsum 24",
        "Lorem Ipsum 25",
        "Lorem Ipsum 26",
        "Lorem Ipsum 27",
        "Lorem Ipsum 28",
        "Lorem Ipsum 29",
        "Lorem Ipsum 30"
    )

    fun randomQuote(): String = quotes.random()
}
