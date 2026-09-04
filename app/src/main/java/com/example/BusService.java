package com.example;

public enum BusService {
    JOTUR(
        "jotur",
        "Jotur",
        "Horários e Mapa Interativo",
        "Linhas metropolitanas integradas de Palhoça, Florianópolis e região. Acesse horários e visualize o mapa interativo de linhas e trajetos.",
        "https://www.jotur.com.br/horarios/",
        true,
        "Com Mapa Interativo",
        0xFF059669
    ),
    CONSORCIO_FENIX(
        "consorcio_fenix",
        "Consórcio Fênix",
        "Horários e Linhas Urbanas",
        "Transporte coletivo de Florianópolis. Consulte quadro de horários completo, partidas em tempo real e itinerários dos ônibus.",
        "https://www.consorciofenix.com.br/horarios",
        false,
        "Florianópolis Urbano",
        0xFFDC2626
    );

    private final String id;
    private final String title;
    private final String subtitle;
    private final String description;
    private final String url;
    private final boolean hasMap;
    private final String tag;
    private final int brandColor;

    BusService(String id, String title, String subtitle, String description, String url, boolean hasMap, String tag, int brandColor) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
        this.url = url;
        this.hasMap = hasMap;
        this.tag = tag;
        this.brandColor = brandColor;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public boolean hasMap() {
        return hasMap;
    }

    public String getTag() {
        return tag;
    }

    public int getBrandColor() {
        return brandColor;
    }

    public static BusService fromId(String id) {
        for (BusService service : values()) {
            if (service.id.equalsIgnoreCase(id)) {
                return service;
            }
        }
        return JOTUR;
    }
}
