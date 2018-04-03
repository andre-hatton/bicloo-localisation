package com.yoshizuka.bicloo

import com.google.android.gms.maps.model.LatLng
import com.yoshizuka.bicloo.models.entities.Position
import com.yoshizuka.bicloo.models.entities.Station
import com.yoshizuka.bicloo.utils.MapUtils
import org.junit.Test

import org.junit.Assert.*

class MainUnitTest {
    
    private val mStations = ArrayList<Station>()
    init {
        setPositionTest()
    }

    /**
     * Test la distance entre 2 points
     */
    @Test
    fun testDistance() {
        val distance = MapUtils.getDistance(LatLng(18.0, 33.0), LatLng(14.0, 36.0))
        assertEquals(5.0, distance, 0.0)
    }

    /**
     * Test que le décodage des polylines google map fonctionne
     */
    @Test
    fun testDecodePoly() {
        val decode = MapUtils.decodePoly("ine_Ht}nHXf@V\\PRVRTHXHLBPBL?LARCh@G`AMb@Ml@O|Ac@v@a@")
        val expected = "[lat/lng: (47.21909,-1.55627), lat/lng: (47.21896,-1.55647), lat/lng: (47.21884,-1.55662), lat/lng: (47.21875,-1.55672), lat/lng: (47.21863,-1.55682), lat/lng: (47.21852,-1.55687), lat/lng: (47.21839,-1.55692), lat/lng: (47.21832,-1.55694), lat/lng: (47.21823,-1.55696), lat/lng: (47.21816,-1.55696), lat/lng: (47.21809,-1.55695), lat/lng: (47.21799,-1.55693), lat/lng: (47.21778,-1.55689), lat/lng: (47.21745,-1.55682), lat/lng: (47.21727,-1.55675), lat/lng: (47.21704,-1.55667), lat/lng: (47.21657,-1.55649), lat/lng: (47.21629,-1.55632)]"
        assertEquals(expected, decode.toString())
    }

    /**
     * Test que la fonction pour recupérer la station la plus proche d'un point donnée donctionne
     */
    @Test
    fun testCloserStation() {
        var myPosition = LatLng(47.2229697, -1.5334502)
        var closerStation = MapUtils.getCloserStation(myPosition, mStations)
        assertEquals(25, closerStation.id)

        myPosition = LatLng(47.23, -1.5334502)
        closerStation = MapUtils.getCloserStation(myPosition, mStations)
        assertEquals(97, closerStation.id)

        myPosition = LatLng(47.210549, -1.567368)
        closerStation = MapUtils.getCloserStation(myPosition, mStations)
        assertEquals(36, closerStation.id)

        // Station la plus proche fermé
        myPosition = LatLng(47.2141946314964, -1.52973588598033)
        closerStation = MapUtils.getCloserStation(myPosition, mStations)
        assertEquals(10, closerStation.id)

        // Station la plus proche n'a pas de vélo siponible
        myPosition = LatLng(47.21921395261352, -1.561570147641708)
        closerStation = MapUtils.getCloserStation(myPosition, mStations)
        assertEquals(15, closerStation.id)

        // Station la plus proche aillant des emplacements disponibles
        myPosition = LatLng(47.21230895884701, -1.571369360221906)
        closerStation = MapUtils.getCloserStation(myPosition, mStations, false)
        assertEquals(74, closerStation.id)
    }

    /**
     * Test que les 3 station les plus proche soit bien celle attendu
     */
    @Test
    fun testClosersStation() {
        val myPosition = LatLng(47.2229697, -1.5334502)
        val closerStation = MapUtils.getClosersStation(3, myPosition, mStations)
        assertEquals(25, closerStation[0].id)
        assertEquals(69, closerStation[1].id)
        assertEquals(10, closerStation[2].id)
    }

    /**
     * Recup d'une liste de station pour les test (poition réel récupérer via l'api) => certaines données incohérentes pour des test d'emplacement
     */
    private fun setPositionTest() {
        mStations.add(Station(10, "00010- PICASSO", "", Position(47.216369613307506, -1.534184544523239), false, false, "OPEN", 18, 11, 7, 1522533803000))
        mStations.add(Station(38, "00038-PLACE RICORDEAU", "", Position(47.212108463141774, -1.553049129320471), true, false, "OPEN", 40, 16, 24, 1522534094000))
        mStations.add(Station(66, "00066-CHANZY", "", Position(47.22542988533347, -1.547999925907375), false, false, "OPEN", 15, 8, 7, 1522534027000))
        mStations.add(Station(3, "00003-STRASBOURG", "", Position(47.216479771820744, -1.55154060534636), false, false, "OPEN", 15, 3, 12, 1522533924000))
        mStations.add(Station(2, "00002-HÃTEL DE VILLE", "", Position(47.21857181309145, -1.553484290392954), false, false, "OPEN", 20, 3, 17, 1522533711000))
        mStations.add(Station(62, "00062-GARE NORD", "", Position(47.218132613316826, -1.542208634875899), true, false, "OPEN", 40, 0, 40, 1522534025000))
        mStations.add(Station(43, "00043 - MACHINE DE L'ÃLE", "", Position(47.20691895869599, -1.564806790782994), true, false, "OPEN", 30, 15, 15, 1522490899000))
        mStations.add(Station(58, "00058-PALAIS DES SPORTS", "", Position(47.208466072191925, -1.535981000861306), false, false, "OPEN", 15, 1, 14, 1522533951000))
        mStations.add(Station(94, "00094-PETIT PORT", "", Position(47.243263914975486, -1.556344610167984), true, false, "OPEN", 20, 19, 1, 1522533635000))
        mStations.add(Station(84, "00084-RÃGION", "", Position(47.21074214218137, -1.525933451925082), false, false, "OPEN", 19, 9, 10, 1522534152000))
        mStations.add(Station(31, "00031-BOURSE", "", Position(47.212083030753185, -1.559086551239872), false, false, "OPEN", 16, 5, 11, 1522533926000))
        mStations.add(Station(74, "00074-CANCLAUX", "", Position(47.21433366088933, -1.574924617192968), true, false, "OPEN", 15, 9, 5, 1522534040000))
        mStations.add(Station(72, "00072-BELLAMY-GUÃ MOREAU", "", Position(47.22862569057461, -1.561806393011127), false, false, "OPEN", 15, 9, 6, 1522533852000))
        mStations.add(Station(40, "00040-MADELEINE", "", Position(47.208804642437784, -1.550411914119152), false, false, "OPEN", 15, 5, 10, 1522534156000))
        mStations.add(Station(22, "00022-MOQUECHIEN", "", Position(47.220392126691955, -1.555531041936261), false, false, "OPEN", 16, 12, 4, 1522533648000))
        mStations.add(Station(19, "00019-SAINT SIMILIEN", "", Position(47.219854467329974, -1.558920890474776), false, false, "OPEN", 15, 15, 0, 1522533739000))
        mStations.add(Station(51, "00051-TABARLY", "", Position(47.2141946314965, -1.52973588598036), false, false, "CLOSED", 20, 0, 0, 1522533733000))
        mStations.add(Station(89, "00089-SAINT-AIGNAN", "", Position(47.20743863754715, -1.578689264310988), false, false, "OPEN", 14, 12, 2, 1522533608000))
        mStations.add(Station(77, "00077-ECOLE D'ARCHITECTURE", "", Position(47.207605640212115, -1.557532467914316), true, false, "OPEN", 25, 20, 5, 1522534112000))
        mStations.add(Station(42, "00042-GARE MARITIME", "", Position(47.20659772898787, -1.572610286970505), true, false, "OPEN", 25, 14, 11, 1522533913000))
        mStations.add(Station(87, "00087-ANATOLE FRANCE", "", Position(47.223604815399085, -1.571373578112414), true, false, "OPEN", 15, 14, 1, 1522534118000))
        mStations.add(Station(47, "00047- MARTYRS NANTAIS", "", Position(47.20702417983412, -1.548161311046709), true, false, "OPEN", 15, 10, 5, 1522533686000))
        mStations.add(Station(102, "00102-GARE PT ROUSSEAU", "", Position(47.19280351379784, -1.549291898557657), true, false, "OPEN", 20, 5, 15, 1522533694000))
        mStations.add(Station(97, "00097-SAINT DONATIEN", "", Position(47.22908808709286, -1.540292594289221), false, false, "OPEN", 20, 6, 14, 1522534135000))
        mStations.add(Station(30, "00030-COMMERCE", "", Position(47.21312055103121, -1.557393235628245), true, false, "OPEN", 39, 12, 27, 1522533622000))
        mStations.add(Station(56, "00056-VINCENT GACHE", "", Position(47.206798618575256, -1.541777739850638), false, false, "OPEN", 15, 4, 11, 1522533955000))
        mStations.add(Station(7, "00007- BARILLERIE", "", Position(47.215331358010445, -1.556126125975167), false, false, "OPEN", 15, 7, 8, 1522533727000))
        mStations.add(Station(27, "00027-GUIST'HAU NORD", "", Position(47.21792981632508, -1.569171883302444), false, true, "OPEN", 15, 12, 3, 1522533629000))
        mStations.add(Station(15, "00015-PLACE ARISTIDE BRIAND", "", Position(47.21718482329023, -1.562986793288367), false, false, "OPEN", 17, 0, 3, 1522534177000))
        mStations.add(Station(83, "00083-MILLERAND", "", Position(47.208199645038825, -1.530180435959972), true, false, "OPEN", 14, 5, 9, 1522533831000))
        mStations.add(Station(45, "00045-PRAIRIE AU DUC", "", Position(47.2047839355992, -1.55892310936669), false, false, "OPEN", 13, 11, 2, 1522533742000))
        mStations.add(Station(34, "00034-MEDIATHEQUE", "", Position(47.210886925815934, -1.561744905652581), true, false, "OPEN", 20, 15, 4, 1522533688000))
        mStations.add(Station(18, "00018-PLACE VIARME", "", Position(47.22142933898549, -1.563308366869757), true, true, "OPEN", 14, 14, 0, 1522533902000))
        mStations.add(Station(91, "00091-RD PT VANNES", "", Position(47.229580605369215, -1.572114577993728), true, false, "OPEN", 18, 12, 4, 1522533728000))
        mStations.add(Station(46, "00046-PLACE REPUBLIQUE", "", Position(47.20532409029517, -1.555183107916184), false, false, "OPEN", 20, 12, 8, 1522533637000))
        mStations.add(Station(95, "00095-MICHELET", "", Position(47.234687038295554, -1.556421138973112), true, false, "OPEN", 25, 21, 4, 1522534083000))
        mStations.add(Station(20, "00020-BELLAMY", "", Position(47.222902006053054, -1.557229797031573), true, false, "OPEN", 15, 12, 3, 1522533724000))
        mStations.add(Station(85, "00085-BEL AIR", "", Position(47.226394430986105, -1.560022819676629), false, true, "OPEN", 14, 11, 3, 1522534051000))
        mStations.add(Station(81, "00081 - MANGIN", "", Position(47.20095551723967, -1.545507251231619), true, false, "OPEN", 14, 6, 8, 1522533588000))
        mStations.add(Station(55, "00055-MAGELLAN", "", Position(47.21078140655944, -1.544298470945214), false, false, "OPEN", 15, 5, 10, 1522533960000))
        mStations.add(Station(78, "00078-DE GAULLE", "", Position(47.20844367718286, -1.540514488290191), false, false, "OPEN", 15, 3, 12, 1522533807000))
        mStations.add(Station(67, "00067-PLACE WALDECK ROUSSEAU", "", Position(47.22777476106513, -1.552147986072217), true, false, "OPEN", 19, 11, 8, 1522533735000))
        mStations.add(Station(92, "00092-DT PT RENNES", "", Position(47.23302818477594, -1.56605906933562), true, false, "OPEN", 20, 17, 3, 1522533636000))
        mStations.add(Station(59, "00059-STADE SAUPIN", "", Position(47.21391161818379, -1.539712888264826), false, false, "OPEN", 15, 4, 11, 1522533670000))
        mStations.add(Station(53, "00053-OLIVETTES", "", Position(47.211736393222054, -1.549886024937758), false, false, "OPEN", 15, 10, 5, 1522534156000))
        mStations.add(Station(57, "00057-GAETAN RONDEAU", "", Position(47.205396132179104, -1.537581863003988), true, false, "OPEN", 20, 3, 16, 1522533960000))
        mStations.add(Station(76, "00076-PLACE RENÃ BOUHIER", "", Position(47.209429092524424, -1.570515336280715), true, false, "OPEN", 15, 0, 6, 1522533580000))
        mStations.add(Station(8, "00008-BOUCHERIE", "", Position(47.21620089258823, -1.557111680197571), true, false, "OPEN", 19, 8, 11, 1522533874000))
        mStations.add(Station(75, "00075-LAMORICIERE", "", Position(47.21230895884701, -1.571369360221906), false, false, "OPEN", 18, 0, 18, 1522533894000))
        mStations.add(Station(73, "00073-SARRADIN", "", Position(47.220882433018865, -1.570438598073934), false, false, "OPEN", 13, 13, 0, 1522533645000))
        mStations.add(Station(25, "00025-DALBY", "", Position(47.223508869811994, -1.529959592703474), true, false, "OPEN", 15, 4, 11, 1522533589000))
        mStations.add(Station(93, "00093-FACULTES", "", Position(47.246298068423485, -1.555309011107301), true, false, "OPEN", 40, 22, 18, 1522534003000))
        mStations.add(Station(44, "00044-PALAIS DE JUSTICE", "", Position(47.20831728844152, -1.561173077989395), false, false, "OPEN", 25, 10, 15, 1522533704000))
        mStations.add(Station(48, "00048-VERDUN", "", Position(47.217505374678645, -1.551849784370311), true, false, "OPEN", 14, 1, 13, 1522533581000))
        mStations.add(Station(1, "00001-PREFECTURE", "", Position(47.220255183724454, -1.55408380740881), true, false, "OPEN", 25, 24, 1, 1522533966000))
        mStations.add(Station(99, "00099-ST JACQUES", "", Position(47.1958989090141, -1.536540663464699), false, false, "OPEN", 15, 0, 15, 1522533785000))
        mStations.add(Station(23, "00023-BUAT", "", Position(47.227145468224634, -1.542404741250403), true, false, "OPEN", 11, 6, 5, 1522533653000))
        mStations.add(Station(80, "00080 - VICTOR HUGO", "", Position(47.204003208491145, -1.551548484803203), false, false, "OPEN", 15, 7, 7, 1522533847000))
        mStations.add(Station(90, "00090-PROCE", "", Position(47.22419506173264, -1.576612843727262), true, false, "OPEN", 15, 9, 5, 1522533598000))
        mStations.add(Station(61, "00061-LU - LIEU UNIQUE", "", Position(47.215591463959825, -1.545905939112105), false, false, "OPEN", 15, 5, 10, 1522533747000))
        mStations.add(Station(32, "00032-FOCH", "", Position(47.2191122, -1.5502297), false, false, "OPEN", 15, 3, 12, 1522533739000))
        mStations.add(Station(17, "00017-SAINTE ELISABETH", "", Position(47.21921395261352, -1.561570147641708), false, true, "OPEN", 14, 14, 0, 1522533695000))
        mStations.add(Station(29, "00029-DUGUAY TROUIN", "", Position(47.213450231365094, -1.555269922656021), false, false, "OPEN", 15, 4, 11, 1522533815000))
        mStations.add(Station(21, "00021-MARCHE TALENSAC SUD", "", Position(47.22114332912908, -1.556476339327795), false, false, "OPEN", 16, 9, 7, 1522534006000))
        mStations.add(Station(50, "00050-CHATEAU", "", Position(47.2151496951626, -1.55022624256337), true, false, "OPEN", 16, 4, 11, 1522534146000))
        mStations.add(Station(37, "00037-FEYDEAU", "", Position(47.211465063355035, -1.5568245371939), false, false, "OPEN", 21, 19, 2, 1522533976000))
        mStations.add(Station(71, "00071-MARCHE TALENSAC NORD", "", Position(47.22185363818837, -1.558940746690739), false, true, "OPEN", 14, 4, 10, 1522533962000))
        mStations.add(Station(86, "00086-HAUTS PAVÃS", "", Position(47.22547062698044, -1.567501842502272), false, false, "OPEN", 16, 13, 3, 1522534085000))
        mStations.add(Station(41, "00041-CHANTIER NAVAL", "", Position(47.2083574798195, -1.56952404874215), false, false, "OPEN", 15, 0, 15, 1522533901000))
        mStations.add(Station(12, "00012-PLACE DELORME", "", Position(47.21515818868574, -1.563900673465861), true, true, "OPEN", 20, 18, 2, 1522533802000))
        mStations.add(Station(98, "00098-HUIT MAI", "", Position(47.18921204751304, -1.550828036456355), true, false, "OPEN", 15, 8, 7, 1522533947000))
        mStations.add(Station(26, "00026-GUIST'HAU SUD", "", Position(47.21682795997449, -1.567004526265165), true, true, "OPEN", 15, 15, 0, 1522533745000))
        mStations.add(Station(52, "00052-BACO", "", Position(47.214327524589145, -1.548750555741977), false, false, "OPEN", 15, 0, 15, 1522533872000))
        mStations.add(Station(39, "00039-QUAI MONCOUSU", "", Position(47.209406573873714, -1.555233144631466), false, false, "OPEN", 19, 18, 1, 1522533847000))
        mStations.add(Station(35, "00035-JEAN V", "", Position(47.212168974412094, -1.565543986075099), false, false, "OPEN", 15, 15, 0, 1522534035000))
        mStations.add(Station(11, "00011-CALVAIRE", "", Position(47.215578942427136, -1.560675283700123), true, true, "OPEN", 15, 11, 4, 1522533705000))
        mStations.add(Station(101, "00101 - PIRMIL", "", Position(47.1965313642396, -1.54156809120326), true, false, "OPEN", 28, 14, 14, 1522533861000))
        mStations.add(Station(63, "00063-JARDIN DES PLANTES", "", Position(47.219834197318974, -1.544718964531585), false, false, "OPEN", 35, 17, 18, 1522534179000))
        mStations.add(Station(16, "00016-PLACE EDOUARD NORMAND", "", Position(47.21902754473837, -1.563419484053747), false, false, "OPEN", 15, 0, 0, 1522534128000))
        mStations.add(Station(100, "00100 - GRENERAIE", "", Position(47.199923092905216, -1.534717540470481), true, false, "OPEN", 20, 4, 16, 1522534042000))
        mStations.add(Station(88, "00088-MELLINET", "", Position(47.211501070117826, -1.577175414321782), false, false, "OPEN", 15, 13, 2, 1522533652000))
        mStations.add(Station(70, "00070-GARE SUD 2", "", Position(47.215505, -1.542844), false, false, "OPEN", 70, 15, 55, 1522533712000))
        mStations.add(Station(4, "00004-MOULIN", "", Position(47.216667582015184, -1.5543847138777), false, false, "OPEN", 13, 10, 3, 1522533610000))
        mStations.add(Station(96, "00096-TORTIERE", "", Position(47.235335222208626, -1.549221766809888), true, false, "OPEN", 16, 12, 2, 1522533632000))
        mStations.add(Station(64, "00064-SAINT CLEMENT", "", Position(47.221785183522634, -1.547099233718495), false, true, "OPEN", 15, 4, 11, 1522533987000))
        mStations.add(Station(54, "00054- CITE INTERNATIONALE DES CONGRES", "", Position(47.213180085527846, -1.544952541668585), true, false, "OPEN", 30, 16, 14, 1522533670000))
        mStations.add(Station(14, "00014-SAINT FÃLIX", "", Position(47.230334, -1.556133), false, false, "OPEN", 14, 9, 5, 1522534132000))
        mStations.add(Station(9, "00009-GUEPIN", "", Position(47.215814587139455, -1.559017485167438), false, false, "OPEN", 14, 12, 2, 1522533783000))
        mStations.add(Station(28, "00028- PLACE DE L'EDIT NANTES", "", Position(47.21446342738575, -1.567568016663838), false, true, "OPEN", 13, 13, 0, 1522533925000))
        mStations.add(Station(33, "00033-RACINE", "", Position(47.213522613719896, -1.563140874431397), false, false, "OPEN", 15, 10, 5, 1522533726000))
        mStations.add(Station(24, "00024-VERSAILLES", "", Position(47.22451490719107, -1.553711961402328), false, false, "OPEN", 15, 12, 3, 1522533749000))
        mStations.add(Station(68, "00068-LIVET", "", Position(47.2240678794875, -1.54521243421509), false, false, "OPEN", 15, 10, 5, 1522534179000))
        mStations.add(Station(65, "00065-COURS SULLY", "", Position(47.222587339906696, -1.551666442545979), true, false, "OPEN", 15, 9, 6, 1522533616000))
        mStations.add(Station(69, "00069 - MANUFACTURE", "", Position(47.219216380711345, -1.535847443276377), false, false, "OPEN", 15, 2, 13, 1522533783000))
        mStations.add(Station(13, "00013-BRETAGNE SUD", "", Position(47.216787949913034, -1.559161066618992), false, true, "OPEN", 20, 18, 2, 1522533905000))
        mStations.add(Station(36, "00036-ALGER", "", Position(47.210549, -1.567368), false, false, "OPEN", 15, 13, 2, 1522533740000))
        mStations.add(Station(82, "00082-SEBILLEAU", "", Position(47.206757512308414, -1.533118244923623), false, false, "OPEN", 16, 4, 12, 1522533672000))
        mStations.add(Station(5, "00005-BROSSARD", "", Position(47.2188372304694, -1.55674926307655), false, false, "OPEN", 15, 1, 14, 1522533928000))
        mStations.add(Station(6, "00006-PLACE DU CIRQUE", "", Position(47.21759668240601, -1.556937645378872), true, false, "OPEN", 15, 2, 13, 1522534155000))
    }
}