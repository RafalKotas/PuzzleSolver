package com.puzzlesolverappbackend.puzzlesolverapp.nonogram.initializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puzzlesolverappbackend.puzzlesolverapp.common.CommonService;
import com.puzzlesolverappbackend.puzzlesolverapp.constants.InitializerConstants;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.Nonogram;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.model.NonogramFileDetails;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.solver.config.GuessMode;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.repository.NonogramRepository;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.service.NonogramLogicService;
import com.puzzlesolverappbackend.puzzlesolverapp.nonogram.service.NonogramService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

import static com.puzzlesolverappbackend.puzzlesolverapp.constants.SharedConstants.JSON_EXTENSION;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.core.rules.NonogramRules.mapNonogramFileDetailsToNonogramRules;
import static com.puzzlesolverappbackend.puzzlesolverapp.nonogram.utils.NonogramStatsUtils.getCompletionPercentage;

@Component
@ConditionalOnProperty(
        prefix = "nonogram.init",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = false
)
@Order(7)
@Slf4j
public class NonogramSolveInitializer implements CommandLineRunner {

    private static final boolean SAVE_SOLUTIONS = true;

    public static final String PUZZLE_PATH = InitializerConstants.PUZZLE_RELATIVE_PATH +
            InitializerConstants.PuzzleMappings.NONOGRAM_PATH_SUFFIX;

    @Autowired
    private NonogramRepository nonogramRepository;

    @Autowired
    NonogramLogicService nonogramLogicService;

    @Autowired
    NonogramService nonogramService;

    @Autowired
    CommonService commonService;

    Nonogram nonogram;
    String filename;
    String source;
    String year;
    String month;
    Double difficulty;
    Integer height;
    Integer width;

    List<Double> difficultyRange;
    Set<String> sources;

    public void initParameters(double minDifficulty, double maxDifficulty, String source) {
        difficultyRange = new ArrayList<>();
        difficultyRange.add(minDifficulty);
        difficultyRange.add(maxDifficulty);

        sources = new HashSet<>();
        sources.add(source);
    }

    @Override
    public void run(String... args) throws Exception {

        initParameters(1.0, 5.0, "logi");
        List<Nonogram> selectedNonogramsList = nonogramRepository.selectNonogramBySourceAndDifficulty(sources,
                difficultyRange.get(0), difficultyRange.get(1));

        selectedNonogramsList = selectedNonogramsList
                .stream()
                .filter(nonogram1 -> nonogram1.getSize().getHeight().equals(30) && nonogram1.getSize().getWidth().equals(30))
                .toList();

        int selectedCount = 0;
        int solvedCount = 0;

        List<Pair<String, Double>> filesWithTimeTooLongSolvingDifficultyTwoLogi = List.of(
                Pair.of("o09976", 11.787),
                Pair.of("o04678", 32.250)
        );

        List<String> filesWithTimeOkDifficultyTwoLogi = List.of();

        List<Pair<String, Double>> filesWithTimeTooLongSolvingDifficultyThreeLogi = List.of(
                Pair.of("o08214", 17.388),
                Pair.of("o05862", 164.811),
                Pair.of("o10335", 48.767),
                Pair.of("o07956", 19.971),
                Pair.of("o08232", 42.038),
                Pair.of("o11507", 118.479),
                Pair.of("o11465", 37.088),
                Pair.of("o07811", 10.713),
                Pair.of("o06554", 11.463),
                Pair.of("o11278", 20.025),
                Pair.of("o10090", 60.769),
                Pair.of("o11487", 28.395),
                Pair.of("o10340", 11.469),
                Pair.of("o10316", 90.630),
                Pair.of("o07953", 13.136),
                Pair.of("o09800", 18.852),
                Pair.of("o09728", 227.544),
                Pair.of("o08198", 24.748),
                Pair.of("o11215", 12.968),
                Pair.of("o11612", 18.972),
                Pair.of("o04761", 19.556),
                Pair.of("o11023", 535.178),
                Pair.of("o08182", 79.576),
                Pair.of("o07948", 88.193),
                Pair.of("o03697", 14.408),
                Pair.of("o08545", 65.254),
                Pair.of("o07939", 32.258),
                Pair.of("o10989", 44.310),
                Pair.of("o10936", 100.840),
                Pair.of("o07798", 124.305),
                Pair.of("o10476", 17.187),
                Pair.of("o10113", 23.955),
                Pair.of("o07939", 32.258),
                Pair.of("o11007", 2341.486),
                Pair.of("o11699", 17.440),
                Pair.of("o11816", 13.477)
        );

        List<String> filesWithTimeOkDifficultyThreeLogi = List.of();

        List<String> filesTooLongSolving = new ArrayList<>();

        List<Pair<String, Double>> filesWithTimeTooLongSolvingDifficultyFourLogi = List.of(
                Pair.of("o11512", 0.446),
                Pair.of("o08178", 0.105),
                Pair.of("o10668", 0.105),
                Pair.of("o08351", 951.247),
                Pair.of("o10631", 0.193),
                Pair.of("o10191", 16.197),
                Pair.of("o07850", 48.004),
                Pair.of("o11435", 16.641),
                Pair.of("o07577", 138.247),
                Pair.of("o07067", 90.932),
                Pair.of("o07536", 15.720),
                Pair.of("o07898", 69.222),
                Pair.of("o08503", 130.960),
                Pair.of("o08747", 14.666),
                Pair.of("o07333", 10.903),
                Pair.of("o09224", 83.781),
                Pair.of("o08320", 380.274),
                Pair.of("o08345", 375.685),
                Pair.of("o10656", 14.995),
                Pair.of("o08438", 134.673),
                Pair.of("o12014", 31.355),
                Pair.of("o08686", 14.316),
                Pair.of("o07949", 14.662),
                Pair.of("o07504", 22.594),
                Pair.of("o07550", 104.862),
                Pair.of("o10105", 17.037),
                Pair.of("o10083", 11.047),
                Pair.of("o07344", 60.576),
                Pair.of("o10999", 88.101),
                Pair.of("o07814", 68.298),
                Pair.of("o10331", 44.834),
                Pair.of("o10489", 34.535),
                Pair.of("o07572", 354.769),
                Pair.of("o07555", 52.196),
                Pair.of("o10112", 1006.832),
                Pair.of("o11155", 408.093),
                Pair.of("o08314", 1231.940),
                Pair.of("o07522", 28.220),
                Pair.of("o07958", 171.917),
                Pair.of("o10669", 446.298),
                Pair.of("o08307", 17.298),
                Pair.of("o11681", 19.994),
                Pair.of("o10638", 247.256),
                Pair.of("o03656", 37.574),
                Pair.of("o11462", 240.753),
                Pair.of("o10144", 456.449),
                Pair.of("o11852", 173.743),
                Pair.of("o08180", 15.898),
                Pair.of("o09543", 54.812),
                Pair.of("o06552", 27.976),
                Pair.of("o07707", 1434.048),
                Pair.of("o08162", 104.073),
                Pair.of("o10096", 87.522),
                Pair.of("o07976", 34.169),
                Pair.of("o08185", 42.171),
                Pair.of("o08168", 22.660),
                Pair.of("o08321", 141.312),
                Pair.of("o07477", 70.381),
                Pair.of("o08132", 60.938),
                Pair.of("o11812", 3319.600),
                Pair.of("o07548", 1076.675),
                Pair.of("o07626", 36.990),
                Pair.of("o10114", 38.876),
                Pair.of("o07280", 34.460),
                Pair.of("o10069", 751.956),
                Pair.of("o10255", 13.390),
                Pair.of("o08681", 12.458),
                Pair.of("o06608", 24.277),
                Pair.of("o08530", 27.261),
                Pair.of("o10117", 19.580),
                Pair.of("o12219", 14.054),
                Pair.of("o11142", 82.061),
                Pair.of("o07525", 45.396),
                Pair.of("o09435", 124.702),
                Pair.of("o10068", 18.242),
                Pair.of("o09485", 88.657),
                Pair.of("o07899", 149.847),
                Pair.of("o11159", 84.168),
                Pair.of("o10932", 11.659),
                Pair.of("o03097", 40.282),
                Pair.of("o10991", 22.667),
                Pair.of("o08676", 17.002),
                Pair.of("o08360", 25.310),
                Pair.of("o07594", 153.977),
                Pair.of("o07810", 29.974),
                Pair.of("o07501", 38.632),
                Pair.of("o11427", 35.744),
                Pair.of("o07833", 15.620),
                Pair.of("o08222", 10.242),
                Pair.of("o07343", 137.371),
                Pair.of("o08026", 776.377),
                Pair.of("o08123", 1509.205),
                Pair.of("o11542", 12.398),
                Pair.of("o08493", 43.212),
                Pair.of("o07978", 66.419),
                Pair.of("o07406", 15.669),
                Pair.of("o11601", 89.373),
                Pair.of("o11630", 618.831),
                Pair.of("o11513", 122.383),
                Pair.of("o10659", 115.068),
                Pair.of("o07860", 124.187),
                Pair.of("o10070", 20.019),
                Pair.of("o07276", 33.114),
                Pair.of("o11798", 10.912),
                Pair.of("o08346", 21.058),
                Pair.of("o07495", 20.127),
                Pair.of("o09828", 10.937),
                Pair.of("o10851", 33.628),
                Pair.of("o07402", 46.640),
                Pair.of("o11177", 2068.865),
                Pair.of("o07985", 200.167),
                Pair.of("o08748", 23.340),
                Pair.of("o10170", 51.768),
                Pair.of("o10153", 586.056),
                Pair.of("o07499", 18.385),
                Pair.of("o08315", 137.070),
                Pair.of("o11430", 88.187),
                Pair.of("o07483", 121.726),
                Pair.of("o10589", 55.825),
                Pair.of("o08421", 29.556),
                Pair.of("o11463", 20.459),
                Pair.of("o11375", 12.593),
                Pair.of("o03300", 32.880),
                Pair.of("o11375", 17.136),
                Pair.of("o11375", 12.593),
                Pair.of("o10573", 50.930),
                Pair.of("o10493", 14.832),
                Pair.of("o08365", 14.820),
                Pair.of("o11141", 10.429),
                Pair.of("o06607", 1070.954),
                Pair.of("o08197", 11.387),
                Pair.of("o09940", 59.697),
                Pair.of("o07160", 13.877),
                Pair.of("o08139", 98.871),
                Pair.of("o07839", 256.790),
                Pair.of("o07160", 194.885),
                Pair.of("o07423", 12.700),
                Pair.of("o07160", 22.954),
                Pair.of("o07332", 832.156),
                Pair.of("o08547", 51.000),
                Pair.of("o08050", 10.929),
                Pair.of("o11511", 84.340),
                Pair.of("o05747", 743.947),
                Pair.of("o10637", 97.957),
                Pair.of("o11460", 14.068),
                Pair.of("o10922", 30.786),
                Pair.of("o11704", 83.757),
                Pair.of("o12324", 145.639),
                Pair.of("o11425", 12.616),
                Pair.of("o09995", 12.998),
                Pair.of("o11457", 842.810),
                Pair.of("o09090", 51.415),
                Pair.of("o12313", 15.324),
                Pair.of("o10306", 10.140),
                Pair.of("o12099", 16.131)
        );

        List<String> filesWithTimeOkDifficultyFourLogi = List.of();

        List<Pair<String, Double>> filesWithTimeTooLongSolvingDifficultyFiveLogi = Arrays.asList(
                Pair.of("o10310", 57.0),
                Pair.of("o07502", 87.0),
                Pair.of("o07518", 66.0),
                Pair.of("o08298", -1.0),      // don't know how long
                Pair.of("o11803", 62.0),
                Pair.of("o07279", 60.0),
                Pair.of("o08623", 59.0),
                Pair.of("o09378", 365.0),
                Pair.of("o07412", 550.0),
                Pair.of("o10073", -1.0),      // don't know how long
                Pair.of("o11543", 480.0),     // ~8min
                Pair.of("o10686", 300.0),     // ~5min
                Pair.of("o07283", -1.0),      // inf
                Pair.of("o07396", -1.0),      // inf
                Pair.of("o11495", -1.0),      // don't know how long
                Pair.of("o08674", 63.5),
                Pair.of("o10988", 52.0),
                Pair.of("o11835", 68.0),
                Pair.of("o11616", 55.3),
                Pair.of("o09476", 219.0),
                Pair.of("o07350", 192.0),
                Pair.of("o08071", 1013.0),
                Pair.of("o07486", 283.0),
                Pair.of("o06941", 60.0),
                Pair.of("o11964", 153.0),
                Pair.of("o07961", 307.0),
                Pair.of("o09471", 118.0),
                Pair.of("o07803", 109.0),
                Pair.of("o07460", 118.0)
        );

        List<String> filesWithTimeOkDifficultyFiveLogi = List.of();

        // katana non-solved heuristically in first version
        List<Pair<String, Double>> filesWithTimeTooLongSolvingKatanaFifteenSquare = Arrays.asList(
                Pair.of("Miś(Bear)(auth_aPeer)", 86.0),
                Pair.of("Snejinka(auth_satt3047)", 67.0),
                Pair.of("Pattern15x15_1(auth_wiki)", 28.0), // Recursion or sth need, Completion 0.89
                Pair.of("Tak(auth_machina_virtualna)", 66.0),
                Pair.of("Pattern(Patroon)(auth_Yayolo)", 52.0),
                Pair.of("Wiatrak(Fan)(auth_Fromasz)", 22.0), // Recursion or sth need, Completion 0.89
                Pair.of("Twirl(auth_Nillerdyret)", 20.0), // Recursion or sth need, Completion 40.89
                Pair.of("Screaming_birdie(auth_Gam)", 106.0),
                Pair.of("Diamond(auth_Amilida)", 70.0), // Recursion or sth need, Completion 21.33
                Pair.of("Pattern(auth_lliyaa)", 25.0),
                Pair.of("Carrelages(auth_Blinda)", 23.593), // Recursion or sth need, Completion 43.11
                Pair.of("Tea(auth_Les)", 32.0),
                Pair.of("Flower(auth_Mirka3)", 59.0),
                Pair.of("Earth_symbol(auth_Naar)", 43.0),
                Pair.of("snowflake(auth_Arina2008Arina)", 31.917),
                Pair.of("Pattern_37(auth_Ariannav)", 84.0),
                Pair.of("Regression(auth_PrefrontalCortex)", 30.858), // Recursion or sth need, Completion 43.56
                Pair.of("Pig(auth_Mark321)", 82.0),
                Pair.of("Spruce(auth_Murzik)", 166.0), // Completion 100.0 - Recursion
                Pair.of("Heart(auth_beclyn)", 50.0), // Recursion or sth need, Completion 30.22
                Pair.of("Pattern_2(auth_wim13)", 91.0), // Recursion or sth need, Completion 13.33
                Pair.of("Magic_Staff(Staff)(auth_Twilia)", 31.0),
                Pair.of("Christmas_Tree(auth_DejaV)", 65.0),
                Pair.of("Cocker_Spaniel(auth_petunya)", 83.0),
                Pair.of("My_weird_patterns_-_LII(auth_Psexanutik)", 26.0), // Recursion or sth need, Completion 1.78
                Pair.of("Lilac(auth_Lisa80lvl)", 63.0),
                Pair.of("Krtek(auth_Mirka3)", 28.0),
                Pair.of("PGR(auth_Pegura)", 84.0), // Completion 100.0 - Recursion
                Pair.of("(kanji)_fire(auth_Sonolumin)", 27.668),
                Pair.of("(kanji)_fire(auth_Sonolumin)", 34.0),
                Pair.of("Sleepy(auth_spock2009)", 92.0),
                Pair.of("Farfalla(auth_ieia)", 107.0), // Completion 100.0 - Recursion
                Pair.of("X-Men(auth_Quodgephelph)", 44.558), // Recursion or sth need, Completion 5.33
                Pair.of("sword_with_aura(auth_PPAPER)", 33.0),
                Pair.of("My_weird_patterns_-_LVIII)(auth_Psexanutik)", 26.108),
                Pair.of("Fancy_Crosshair(auth_Alexander_Aguirre)", 52.0), // Completion 100.0 - Recursion
                Pair.of("Lilie(auth_FanLinkin)", 34.0),
                Pair.of("Masterchief_helmet(Halo)(auth_Pate)", 45.0),
                Pair.of("Bottle(auth_Scripty)", 79.0), // Completion 100.0 - Recursion
                Pair.of("Another_challenge_for_you(auth_Psexanutik)", 27.0),
                Pair.of("Sword(auth_Darien_Fawkes)", 23.0),
                Pair.of("Mouse(auth_Puzzhorn)", 26.0),
                Pair.of("Wild_West(auth_Ed95206)", 24.0),
                Pair.of("Escakeras(auth_El...)", 21.0),
                Pair.of("Staircase(auth_Lullabeauxbug)", 29.0),
                Pair.of("Pig(auth_Liuna)", 21.0),
                Pair.of("Pattern15x15_2(auth_wiki)", 20.0),
                Pair.of("Rocket(Auth_AlexPo03011975)", 19.9),
                Pair.of("Pattern3(auth_OmaMor)", 28.199),
                Pair.of("Baby_Stroller(Kingerwagen)(auth_Lostir)", 17.719),
                Pair.of("Butterfly(Papillon)(auth_isiem)", 29.316),
                Pair.of("Krest(auth_JannaS)", 50.398),
                Pair.of("Pattern15x15_2(auth_KILK_UHA)", 32.59),
                Pair.of("Vorobey(auth_Pechenka.)", 43.574),
                Pair.of("Pattern(auth_Zettoo_Lei)", 28.654),
                Pair.of("pattern(auth_Alexys12345)", 31.726),
                Pair.of("Dying_bush(auth_Kateivas)", 31.648),
                Pair.of("Targeting_computer(auth_m.junior)", 59.171), // Recursion or sth need, Completion 18.22
                Pair.of("ghost(auth_anna3337777)", 62.438),
                Pair.of("Scissors(Schere)(auth_Zarathustra)", 71.446), // Recursion or sth need, Completion 12.44
                Pair.of("Flower(auth_Rain)", 42.792),
                Pair.of("Curl(auth_mystery_soul)", 139.058),
                Pair.of("Scissors(Schere)(auth_Zarathustra)", 69.792), // Recursion or sth need, Completion 14.67
                Pair.of("pattern_1(auth_Erfina)", 73.195), // Recursion or sth need, Completion 23.11
                Pair.of("Bracelet(auth_DrewSamson)", 39.554),
                Pair.of("Chick(auth_keikyu2100gata)", 28.858),
                Pair.of("Uzor(auth_Shurik702)", 29.993),
                Pair.of("The_allseeing_eye(auth_Donovan)", 48.193),
                Pair.of("Butterfly(Mariposa)(auth_zupermami)", 35.591),
                Pair.of("Goat(auth_MissMijo)", 54.377),
                Pair.of("Stamp(Estampa)(auth_Nunovni)", 83.473), // Recursion or sth need, Completion 14.67
                Pair.of("Desert_Highway(auth_BlindWanderer)", 55.287), // Recursion or sth need, Completion 2.67
                Pair.of("Snowflake(auth_Evgenyi)", 65.0), // Completion 100.0 - Recursion
                Pair.of("flower(auth_snowyowl8)", 111.076), // Recursion or sth need, Completion 25.33
                Pair.of("Pattern(auth_Yennefer17)", 54.499), // Recursion or sth need, Completion 43.11
                Pair.of("Web(auth_Vitaliy_Lokos)", 26.45),
                Pair.of("Enda(auth_naaaa)", 41.393),
                Pair.of("Feather(auth_Annsan)", 98.0), // Completion 100.0 - Recursion
                Pair.of("Stripes(auth_Mag&3)", 26.032),
                Pair.of("Argentine_Mate(Mate_Argentino)(auth_Alud)", 20.396),
                Pair.of("Little_bird(P1)(auth_charly65)", 52.654),
                Pair.of("Pattern(auth_KILK_UHA)", 167.889), // Recursion or sth need, Completion 40.89
                Pair.of("Clover(auth_cyndidee)", 93.0) // Completion 100.0 - Recursion
        );

        List<Pair<String, Double>> filesWithTimeOkKatanaFifteenSquare = List.of();

        List<Pair<String, Double>> filesWithTimeTooLongSolvingKatanaTwentySquare = List.of(
                Pair.of("Doggy(Chiot)(auth_Liaaaaaaaaaa)", 2.054),
                Pair.of("Halberd(auth_Aurelius)", 2.321),
                Pair.of("White_cross(auth_Sanchez_Solver)", 0.605),
                Pair.of("Mozaika(auth_ka_ha)", 0.151),
                Pair.of("Tarcza(auth_tabaq)", 0.762),
                Pair.of("wifi(auth_mana)", 0.826),
                Pair.of("Rebellion(auth_Spiderlux)", 0.268),
                Pair.of("pattern(auth_jh1318)", 1.602),
                Pair.of("Don't_know(auth_Y88n_)", 0.478),
                Pair.of("Palm(auth_mmaga)", 0.975),
                Pair.of("Mozaic_1(auth_wim13)", 1.570),
                Pair.of("Girl(auth_Yucia)", 0.391),
                Pair.of("KGungnir(auth_Super_hitman)", 0.447),
                Pair.of("Flower(auth_eil5026)", 0.304),
                Pair.of("Coffee_jug(auth_Ricarix)", 0.774),
                Pair.of("Coal_Waggon(auth_Triple_S)", 0.251),
                Pair.of("Clown(auth_chopper)", 0.253),
                Pair.of("Pattern(auth_greenmusic)", 0.774)
        );

        List<Pair<String, Double>> filesWithTimeOkKatanaTwentySquare = List.of(
                Pair.of("Doily(auth_Ricarix)", 141.034),
                Pair.of("Knight(auth_DrTimer)", 84.636),
                Pair.of("Pocket_bouquet(auth_inush)", 184.109),
                Pair.of("Links(auth_Ricarix)", 69.375),
                Pair.of("Geo_Abstract(auth_Turfer)", 1.585),
                Pair.of("Rune(auth_Mihail102)", 348.304),
                Pair.of("Owl(auth_Yanire)", 1.858),
                Pair.of("Little_star(auth_Force_of_Nature)", 467.579),
                Pair.of("Bumpy_1989_y.(Game)(auth_Milana.)", 23.429),
                Pair.of("Sword(auth_Izuri_Natsuki)", 117.306),
                Pair.of("Entrance(auth_The_Dimmon0811)", 13.318),
                Pair.of("Island(auth_Lexruss)", 141.846),
                Pair.of("Baymax(auth_chopper)", 285.787),
                Pair.of("Simmetria3(auth_mrs.zenzy)", 4.645),
                Pair.of("Ficus_elastica(Gummibaum)(auth_FairyWings29)", 32.275),
                Pair.of("Pedestal(auth_igormart)", 26.94),
                Pair.of("Puzzle_2(auth_Eccentric)", 6.221),
                Pair.of("Muster(auth_lisa&)", 15.198),
                Pair.of("jellyfish(auth_dydh1)", 86.415),
                Pair.of("Spirale(auth_Whitiger13)", 45.266),
                Pair.of("Pattern_9(auth_wim13)", 5.175),
                Pair.of("Gecko(auth_Neeky)", 56.052),
                Pair.of("Uzory_Taimyra_3(auth_untaika)", 135.863),
                Pair.of("Squares(auth_bakagiggio)", 135.042),
                Pair.of("Crabe(auth_D93)", 82.003),
                Pair.of("Teddy_Bear(auth_dizziness)", 109.376),
                Pair.of("Trumpet(auth_sydmoney42)", 64.994),
                Pair.of("Screw(auth_Feiry)", 125.888),
                Pair.of("Nothing(auth_mijo)", 192.251),
                Pair.of("Samurai(auth_Nomad)", 112.158),
                Pair.of("wintertree(winterboom)(auth_sterredag)", 78.469),
                Pair.of("Roza_vetrov(auth_Dittodendron)", 31.291),
                Pair.of("Square(hard_for_computer_easy_for_human)(auth_Gcpsu)", 479.165),
                Pair.of("Snowman(auth_Ladyweed)", 408.659),
                Pair.of("Mini_evil(auth_Mefis)", 94.979),
                Pair.of("Needle_and_Button(auth_Swapnil_Bankar)", 95.942),
                Pair.of("Insect(Insetto)(auth_mrs.zenzy)", 143.369),
                Pair.of("X-MAS_TREE(Kerstboom)(auth_Dientje85)", 96.134),
                Pair.of("Circle(Cirkel)(auth_Bro94)", 23.279),
                Pair.of("Clock(auth_rasolinenet1988)", 329.324),
                Pair.of("Chain_Link(auth_BlueRolex)", 128.712),
                Pair.of("Mosaic(auth_eil5026)", 63.765),
                Pair.of("4(Sword)(auth_XXIxDUDxIXX))", 85.564),
                Pair.of("Cross(auth_KILK_UHA))", 253.694),
                Pair.of("Piggy(Schweinchen)(auth_Flauschel)", 215.071),
                Pair.of("Danseurs(auth_louiis)", 18.165),
                Pair.of("Note_key(auth_Marryka11)", 14.7),
                Pair.of("Forest(Wald)(auth_Arinome)", 11.255),
                Pair.of("Horse(auth_Avtogragdanka)", 55.733),
                Pair.of("Birdbox(Caja_nido)(auth_Caminero)", 138.864),
                Pair.of("Knit(Tricot)(auth_gennao)", 6.314),
                Pair.of("Battle_hammer(auth_money_D_luffy)", 11.26),
                Pair.of("The_King(auth_Blizzard)", 27.191),
                Pair.of("Vase(auth_kronki)", 122.524),
                Pair.of("Symetry(Simetria)(auth_rockejr)", 125.72),
                Pair.of("Rabbit(auth_Victor_304)", 96.81),
                Pair.of("Pattern20x20_1(auth_wiki)", 19.347),
                Pair.of("Fish(auth_Santalina)", 218.802),
                Pair.of("Kitty(auth_Gragdanochka)", 64.303),
                Pair.of("Sym-design(auth_Ankit_Seth))", 1.27),
                Pair.of("Soccer_player(auth_Utsu)", 21.438),
                Pair.of("The_world_is_one_family(Vasudaiva_kutumbakam)(auth_Aditya_Deshmukh)", 6.187),
                Pair.of("Mt._FUJI(auth_kamegon)", 9.418),
                Pair.of("Squirrel(auth_Trofimka2210)", 422.999),
                Pair.of("In_a_Heartbeat(auth_Smartypants)", 114.47),
                Pair.of("Love(auth_Callie_Cassidy)", 147.555),
                Pair.of("Squirtle(auth_Scarheart)", 47.638),
                Pair.of("Chertik(auth_Natik2002)", 212.89),
                Pair.of("Simmetria9(auth_mrs.zenzy))", 8.551),
                Pair.of("Flying_bird(auth_Ladyweed)", 254.866),
                Pair.of("Flower20x20(auth_eil5026)", 24.19),
                Pair.of("Parrot(auth_Natali_san)", 25.262),
                Pair.of("Morkovka(auth_Yaroslav_Pidgurskyi)", 2.079),
                Pair.of("Simmetria15(auth_mrs.zenzy)", 140.798),
                Pair.of("Pattern_66(auth_Kleopatra)", 6.495),
                Pair.of("Ghost(auth_Kyte)", 245.687),
                Pair.of("Native_American(Indianer)(auth_gojira)", 181.139),
                Pair.of("Smile(auth_whenwolf)", 40.562),
                Pair.of("Fish(auth_IrinaYa)", 4.577),
                Pair.of("KEngine_piston(Pistone,_fascia_e_perno)(auth_Sigmundd)", 134.346),
                Pair.of("Sleeping_cat(auth_Juli-ma)", 56.502),
                Pair.of("Tri(auth_st3fn0)", 226.002),
                Pair.of("Znak_drogowy(auth_an84na)", 84.176),
                Pair.of("Flying_monster(walk)(auth_Gipnoza)", 79.2),
                Pair.of("Llama_Love(auth_SunKissed)", 19.515),
                Pair.of("Hmmmph(auth_QuinnO)", 63.739),
                Pair.of("1+1(auth_Russell2012)", 222.073),
                Pair.of("Pattern4(auth_wiki)", 263.397),
                Pair.of("Fingerwaves(Onde_Giapponesi)(auth_Fusettini)", 19.384),
                Pair.of("Kanji_for_fire(huo3)(auth_ai2022ncg)", 148.163),
                Pair.of("Celtic_cross(auth_Zipfish)", 92.224),
                Pair.of("Pattern20x20(auth_wiki)", 465.87),
                Pair.of("Curious_monkey(Neugiriger_Affe)(Auth_Fuzzly)", 55.25),
                Pair.of("Heart(auth_Svar)", 35.216),
                Pair.of("Jellyfish(auth_el1s3n)", 12.962),
                Pair.of("Kaleidoscope(Kaleidoskop)(auth_MarStav)", 71.45),
                Pair.of("Gold(auth_STK)", 14.343),
                Pair.of("baseball(auth_Devikajoy)", 255.194),
                Pair.of("Kat(auth_Happy__Girll)", 33.886),
                Pair.of("Cute_dog(auth_Hawka)", 194.954),
                Pair.of("Sunset(auth_pearlrose)", 116.217),
                Pair.of("Bow_and_arrow(auth_Ankit_Seth)", 171.357),
                Pair.of("Fist(auth_Alturo)", 3.48),
                Pair.of("Pick_axe(auth_Kitticats)", 80.823),
                Pair.of("Flower_on_a_vase(Flor_no_vaso)(auth_Marilha)", 577.847),
                Pair.of("House(Maison)(auth_Shusy)", 116.657),
                Pair.of("Baseball(auth_Doctor_J)", 84.049),
                Pair.of("Horse(auth_Caballo)", 262.745),
                Pair.of("Whack-a-mole(Acchiappa_la_talpa)(auth_XCloud92)", 59.95),
                Pair.of("Pisces(auth_boobie420)", 185.992),
                Pair.of("Kilroy_Was_Here(auth_TK421)", 5.348),
                Pair.of("Cap's_Shield(Lo_scudo_del_Capitano)(auth_PC37)", 65.433),
                Pair.of("Key(auth_dr_pure)", 30.252),
                Pair.of("Dagger(auth_PPAPER)", 119.122),
                Pair.of("Cubes(auth_NDee)", 1074.434),
                Pair.of("Jellyfish(auth_byiguana)", 13.983),
                Pair.of("Sword(auth_TAGVoar)", 131.329),
                Pair.of("Sword(auth_Vojta_Vavrik)", 263.962),
                Pair.of("Taurus(auth_GalyaAs)", 2.836),
                Pair.of("Rosa_dos_ventos(auth_Desinho)", 262.412),
                Pair.of("Star(auth_Aleris)", 145.601),
                Pair.of("Design(auth_tkocer17)", 62.532),
                Pair.of("Rose(auth_iStudent)", 83.276),
                Pair.of("Ovni(auth_Porra)", 115.971),
                Pair.of("Design_2(auth_SunKissed)", 391.164),
                Pair.of("Bear(auth_Murzik)", 13.181),
                Pair.of("Butterfly(auth_masha.ff)", 93.701),
                Pair.of("Which_line_is_longest(auth_Samlls)", 231.53),
                Pair.of("Star(Stern)(auth_sisika)", 4.361),
                Pair.of("Sword(auth_AnkebuT35)", 206.525),
                Pair.of("Cat(auth_Nec)", 134.165),
                Pair.of("illusion(auth_efam)", 124.629),
                Pair.of("Ddd(auth_bro_ry)", 37.064),
                Pair.of("A37(auth_Ulti)", 141.072),
                Pair.of("Pattern_8(auth_wim13)", 1.879),
                Pair.of("Cat_in_the_sunshine(auth_aaa1)", 116.666),
                Pair.of("Gothic_cat(auth_Auf228)", 256.059),
                Pair.of("Duck(auth_Yurik)", 1447.235),
                Pair.of("Pattern_91(auth_Irene_009)", 8.857),
                Pair.of("Stsr(auth_Igaryok)", 25.521),
                Pair.of("Wisdom_knot(Nyansapo)(auth_AhmetBayirli)", 82.738),
                Pair.of("dandelion(auth_Gragdanochka)", 2.295),
                Pair.of("King(auth_DrTimer)", 38.169),
                Pair.of("Cubo(auth_Jasson_Manuel)", 12.138),
                Pair.of("Eye_monster(auth_David_M)", 216.687),
                Pair.of("Snail(Schnecke)(auth_bandenklette)", 447.348),
                Pair.of("Flower(Fleur)(auth_cyn86)", 88.5),
                Pair.of("Boat(auth_Thomas_van_Driel)", 6.846),
                Pair.of("Branch(auth_Arahnia)", 2.357),
                Pair.of("Design_01(auth_Nancy_McCrary)", 338.059),
                Pair.of("Good_boy(auth_SayHi)", 97.466),
                Pair.of("Star(auth_Ulti)", 133.921),
                Pair.of("Cats(auth_blisster)", 89.726),
                Pair.of("Foot(Planta_del_pie)(auth_Ladyweed)", 635.799),
                Pair.of("Optical_pattern(auth_Purr_Norris)", 97.278),
                Pair.of("Snail(Schnecke)(auth_bandenklette)", 245.366),
                Pair.of("Tiles(auth_Miss_Annasita)", 3.102),
                Pair.of("Kite(auth_Tatyana30)", 49.206),
                Pair.of("Graveyard(auth_Engorged)", 94.294),
                Pair.of("bismuth_patterns(auth_masekre)", 101.41),
                Pair.of("Forks_4(auth_Arik_Manley)", 14.737),
                Pair.of("Forks_2(auth_Arik_Manley)", 59.097),
                Pair.of("floral_tile(auth_bizibody)", 481.528),
                Pair.of("Pattern_10_Hard(gypsyfyed333(pub)(auth_Kimberly_Edens)", 1.602)
        );

        log.info("Selected nonograms count: {}", selectedCount);

        NonogramFileDetails nonogramFileDetails;
        NonogramRules nonogramRules;
        NonogramLogic nonogramLogicToSolve;
        NonogramLogic nonogramLogicSolved;

        int nonogramNo = 1;


        for (Nonogram selectedNonogram : selectedNonogramsList) {
            difficulty = selectedNonogram.getDifficulty();
            filename = selectedNonogram.getFilename();
            height = selectedNonogram.getSize().getHeight();
            month = selectedNonogram.getPublication().getMonth();
            source = selectedNonogram.getSource();
            width = selectedNonogram.getSize().getWidth();
            year = selectedNonogram.getPublication().getYear();

            ObjectMapper objectMapper = new ObjectMapper();
            nonogramFileDetails = objectMapper.readValue(
                    new File(PUZZLE_PATH + filename + JSON_EXTENSION), NonogramFileDetails.class
            );

            nonogramRules = mapNonogramFileDetailsToNonogramRules(nonogramFileDetails);
            nonogramLogicToSolve = new NonogramLogic(nonogramRules, GuessMode.DISABLED);

            if (!filesTooLongSolving.contains(filename)) {
                long start = System.currentTimeMillis();
                nonogramLogicSolved = nonogramLogicService.runSolverWithCorrectnessCheck(nonogramLogicToSolve,
                        filename + JSON_EXTENSION);
                long finish = System.currentTimeMillis();
                long timeElapsed = finish - start;
                double secondsElapsed = timeElapsed / 1000.0;

                log.info("{}s {}%", secondsElapsed, getCompletionPercentage(nonogramLogicSolved));

                if (getCompletionPercentage(nonogramLogicSolved) == 100) {
                    solvedCount = solvedCount + 1;
                    if (SAVE_SOLUTIONS) {
                        nonogramService.saveSolutionToFile(filename, nonogramLogicSolved.getNonogramSolutionBoard());
                    }
                }
                selectedCount = selectedCount + 1;
            }

            nonogramNo = nonogramNo + 1;
        }

        log.info("Solved count: {}", solvedCount);
        double percentageSolved = selectedCount != 0 ?
                Math.round(((double)(solvedCount) / selectedCount) * 10000 ) / 100.0 : 0.0;
        log.info("Percentage solved: {}", percentageSolved);
    }
}