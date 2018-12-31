package com.shashavs.sample.dummy

data class Dummy(val id: String, val title: String, val description: String, val x: Float, val y: Float)

class DummyData() {

    val data1 = arrayListOf(
        Dummy("001", "001", "description_001", 100f, 120f),
        Dummy("002", "002", "description_002", 50f, 250f),
        Dummy("003", "003", "description_003", 50f, 450f),
        Dummy("004", "004", "description_004", 100f, 600f),

        Dummy("005", "005", "description_005", 350f, 700f),
        Dummy("006", "006", "description_006", 600f, 700f),
        Dummy("007", "007", "description_007", 850f, 700f),

        Dummy("008", "008", "description_008", 1100f, 600f),
        Dummy("009", "009", "description_009", 1150f, 450f),
        Dummy("010", "010", "description_010", 1150f, 250f),
        Dummy("011", "011", "description_011", 1100f, 120f),

        Dummy("012", "012", "description_012", 850f, 60f),
        Dummy("013", "013", "description_013", 600f, 60f),
        Dummy("014", "014", "description_014", 350f, 60f)
    )

    val data2 = arrayListOf(
        Dummy("001", "001", "description_001", 185f, 125f),
        Dummy("002", "002", "description_002", 185f, 565f),
        Dummy("003", "003", "description_003", 490f, 440f),
        Dummy("004", "004", "description_004", 615f, 295f),
        Dummy("005", "005", "description_005", 1015f, 385f),
        Dummy("006", "006", "description_006", 1235f, 275f),
        Dummy("007", "007", "description_007", 1235f, 430f),
        Dummy("008", "008", "description_008", 825f, 75f),
        Dummy("009", "009", "description_009", 465f, 620f),
        Dummy("010", "010", "description_010", 1195f, 620f),
        Dummy("011", "011", "description_011", 1455f, 155f),
        Dummy("012", "012", "description_012", 1455f, 560f)
    )
}