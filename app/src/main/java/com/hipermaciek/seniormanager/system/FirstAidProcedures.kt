package com.hipermaciek.seniormanager.system

object FirstAidProcedures {
    val procedures: Map<String, List<String>> = mapOf(
        "upadek" to listOf(
            "Sprawdź przytomność seniora.",
            "Zabezpiecz miejsce zdarzenia.",
            "Nie podnoś gwałtownie seniora.",
            "Oceń ból i krwawienie.",
            "W razie ryzyka urazu dzwoń 112."
        ),
        "ból w klatce" to listOf(
            "Posadź seniora w wygodnej pozycji.",
            "Sprawdź oddech i tętno.",
            "Podaj zalecone leki doraźne, jeśli są.",
            "Nie zostawiaj seniora samego.",
            "Natychmiast dzwoń 112."
        )
    )
}
