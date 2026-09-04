package com.example

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.FenixRed
import com.example.ui.theme.JoturGreen

enum class BusService(
    val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val url: String,
    val hasMap: Boolean,
    val tag: String,
    val brandColor: Color
) {
    JOTUR(
        id = "jotur",
        title = "Jotur",
        subtitle = "Horários e Mapa Interativo",
        description = "Linhas metropolitanas integradas de Palhoça, Florianópolis e região. Acesse horários e visualize o mapa interativo de linhas e trajetos.",
        url = "https://www.jotur.com.br/horarios/",
        hasMap = true,
        tag = "Com Mapa Interativo",
        brandColor = JoturGreen
    ),
    CONSORCIO_FENIX(
        id = "consorcio_fenix",
        title = "Consórcio Fênix",
        subtitle = "Horários e Linhas Urbanas",
        description = "Transporte coletivo de Florianópolis. Consulte quadro de horários completo, partidas em tempo real e itinerários dos ônibus.",
        url = "https://www.consorciofenix.com.br/horarios",
        hasMap = false,
        tag = "Florianópolis Urbano",
        brandColor = FenixRed
    )
}
