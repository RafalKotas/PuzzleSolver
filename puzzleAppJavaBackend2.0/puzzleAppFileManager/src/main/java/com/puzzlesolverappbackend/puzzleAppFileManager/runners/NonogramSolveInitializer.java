package com.puzzlesolverappbackend.puzzleAppFileManager.runners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puzzlesolverappbackend.puzzleAppFileManager.model.Nonogram;
import com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.NonogramFileDetails;
import com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.NonogramService;
import com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic.NonogramLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.puzzlespecific.nonogram.logic.NonogramLogicService;
import com.puzzlesolverappbackend.puzzleAppFileManager.repository.NonogramRepository;
import com.puzzlesolverappbackend.puzzleAppFileManager.services.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.puzzlesolverappbackend.puzzleAppFileManager.constants.SharedConsts.JSON_EXTENSION;

//@Component
//@Order(7)
public class NonogramSolveInitializer implements CommandLineRunner {

    private final boolean saveSolutions = true;

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
    public final static String puzzlePath = InitializerConstants.PUZZLE_RELATIVE_PATH +
            InitializerConstants.PuzzleMappings.NONOGRAM_PATH_SUFFIX;

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

        initParameters(1.0, 5.0, "katana");
        List<Nonogram> selectedNonogramsList = nonogramRepository.selectNonogramBySourceAndDifficulty(sources,
                difficultyRange.get(0), difficultyRange.get(1));

        selectedNonogramsList = selectedNonogramsList
                .stream()
                .filter(nonogram1 -> nonogram1.getHeight().equals(30) && nonogram1.getWidth().equals(30))
                .toList();

        int selectedCount = 0;
        int solvedCount = 0;

        List<String> filesTooLongSolving = new ArrayList<>();
        //Difficulty 2 logi:
        //filesTooLongSolving.add("o09976"); // 11.787s
        //filesTooLongSolving.add("o04678"); //  32.250s
        //Difficulty 3 logi:
//        filesTooLongSolving.add("o08214"); //  17.388s
//        filesTooLongSolving.add("o05862"); // 164.811s
//        filesTooLongSolving.add("o10335"); //  48.767s
//        filesTooLongSolving.add("o07956"); //  19.971s
//        filesTooLongSolving.add("o08232"); //  42.038s
//        filesTooLongSolving.add("o11507"); // 118.479s
//        filesTooLongSolving.add("o11465"); //  37.088s
//        filesTooLongSolving.add("o07811"); //  10.713s
//        filesTooLongSolving.add("o06554"); //  11.463s
//        filesTooLongSolving.add("o11278"); //  20.025s
//        filesTooLongSolving.add("o10090"); //  60.769s
//        filesTooLongSolving.add("o11487"); //  28.395s
//        filesTooLongSolving.add("o10340"); //  11.469s
//        filesTooLongSolving.add("o10316"); //  90.630s
//        filesTooLongSolving.add("o07953"); //  13.136s
//        filesTooLongSolving.add("o09800"); //  18.852s
//        filesTooLongSolving.add("o09728"); // 227.544s
//        filesTooLongSolving.add("o08198"); //  24.748s
//        filesTooLongSolving.add("o11215"); //  12.968s
//        filesTooLongSolving.add("o11612"); //  18.972s
//        filesTooLongSolving.add("o04761"); //  19.556s
//        filesTooLongSolving.add("o11023"); // 535.178s
//        filesTooLongSolving.add("o08182"); //  79.576s
//        filesTooLongSolving.add("o07948"); //  88.193s
//        filesTooLongSolving.add("o03697"); //  14.408s
//        filesTooLongSolving.add("o08545"); //  65.254s
//        filesTooLongSolving.add("o07939"); //  32.258s
//        filesTooLongSolving.add("o10989"); //  44.310s
//        filesTooLongSolving.add("o10936"); // 100.840s
//        filesTooLongSolving.add("o07798"); // 124.305s
//        filesTooLongSolving.add("o10476"); //  17.187s
//        filesTooLongSolving.add("o10113"); //  23.955s
//        filesTooLongSolving.add("o07939"); //  32.258s
//        filesTooLongSolving.add("o11007"); //2341.486s
//        filesTooLongSolving.add("o11699"); //  17.440s
//        filesTooLongSolving.add("o11816"); //  13.477s
        //Difficulty 4 logi:
//        filesTooLongSolving.add("o11512"); // 0.446s //completion 12.22%
//        filesTooLongSolving.add("o08178"); // 0.105s //completion 8.38%
//        filesTooLongSolving.add("o10668"); // 0.105s //completion 36.42%
//        filesTooLongSolving.add("o08351"); // 951.247s //completion 36.49%
//        filesTooLongSolving.add("o10631"); // 0.193s //completion 31.29%
//
//        filesTooLongSolving.add("o10191"); //  16.197s
//        filesTooLongSolving.add("o07850"); //  48.004s
//        filesTooLongSolving.add("o11435"); //  16.641s
//        filesTooLongSolving.add("o07577"); // 138.247s
//        filesTooLongSolving.add("o07067"); //  90.932s
//        filesTooLongSolving.add("o07536"); //  15.720s
//        filesTooLongSolving.add("o07898"); //  69.222s
//        filesTooLongSolving.add("o08503"); // 130.960s
//        filesTooLongSolving.add("o08747"); //  14.666s
//        filesTooLongSolving.add("o07333"); //  10.903s
//        filesTooLongSolving.add("o09224"); //  83.781s
//        filesTooLongSolving.add("o08320"); // 380.274s
//        filesTooLongSolving.add("o08345"); // 375.685s
//        filesTooLongSolving.add("o10656"); //  14.995s
//        filesTooLongSolving.add("o08438"); // 134.673s
//        filesTooLongSolving.add("o12014"); //  31.355s
//        filesTooLongSolving.add("o08686"); //  14.316s
//        filesTooLongSolving.add("o07949"); //  14.662s
//        filesTooLongSolving.add("o07504"); //  22.594s
//        filesTooLongSolving.add("o07550"); // 104.862s
//        filesTooLongSolving.add("o10105"); //  17.037s
//        filesTooLongSolving.add("o10083"); //  11.047s
//        filesTooLongSolving.add("o07344"); //  60.576s
//        filesTooLongSolving.add("o10999"); //  88.101s
//        filesTooLongSolving.add("o07814"); //  68.298s
//        filesTooLongSolving.add("o10331"); //  44.834s
//        filesTooLongSolving.add("o10489"); //  34.535s
//        filesTooLongSolving.add("o07572"); // 354.769s
//        filesTooLongSolving.add("o07555"); //  52.196s
//        filesTooLongSolving.add("o10112"); //1006.832s
//        filesTooLongSolving.add("o11155"); // 408.093s
//        filesTooLongSolving.add("o08314"); //1231.940s
//        filesTooLongSolving.add("o07522"); //  28.220s
//        filesTooLongSolving.add("o07958"); // 171.917s
//        filesTooLongSolving.add("o10669"); // 446.298s
//        filesTooLongSolving.add("o08307"); //  17.298s
//        filesTooLongSolving.add("o11681"); //  19.994s
//        filesTooLongSolving.add("o10638"); // 247.256s
//        filesTooLongSolving.add("o03656"); //  37.574s
//        filesTooLongSolving.add("o11462"); // 240.753s
//        filesTooLongSolving.add("o10144"); // 456.449s
//        filesTooLongSolving.add("o11852"); // 173.743s
//        filesTooLongSolving.add("o08180"); //  15.898s
//        filesTooLongSolving.add("o09543"); //  54.812s
//        filesTooLongSolving.add("o06552"); //  27.976s
//        filesTooLongSolving.add("o07707"); //1434.048s
//        filesTooLongSolving.add("o08162"); // 104.073s
//        filesTooLongSolving.add("o10096"); //  87.522s
//        filesTooLongSolving.add("o07976"); //  34.169s
//        filesTooLongSolving.add("o08185"); //  42.171s
//        filesTooLongSolving.add("o08168"); //  22.660s
//        filesTooLongSolving.add("o08321"); // 141.312s
//        filesTooLongSolving.add("o07477"); //  70.381s
//        filesTooLongSolving.add("o08132"); //  60.938s
//        filesTooLongSolving.add("o11812"); //  3319.6s
//        filesTooLongSolving.add("o07548"); //1076.675s
//        filesTooLongSolving.add("o07626"); //  36.990s
//        filesTooLongSolving.add("o10114"); //  38.876s
//        filesTooLongSolving.add("o07280"); //  34.460s
//        filesTooLongSolving.add("o10069"); // 751.956s
//        filesTooLongSolving.add("o10255"); //  13.390s
//        filesTooLongSolving.add("o08681"); //  12.458s
//        filesTooLongSolving.add("o06608"); //  24.277s
//        filesTooLongSolving.add("o08530"); //  27.261s
//        filesTooLongSolving.add("o10117"); //  19.580s
//        filesTooLongSolving.add("o12219"); //  14.054s
//        filesTooLongSolving.add("o11142"); //  82.061s
//        filesTooLongSolving.add("o07525"); //  45.396s
//        filesTooLongSolving.add("o09435"); // 124.702s
//        filesTooLongSolving.add("o10068"); //  18.242s
//        filesTooLongSolving.add("o09485"); //  88.657s
//        filesTooLongSolving.add("o07899"); // 149.847s
//        filesTooLongSolving.add("o11159"); //  84.168s
//        filesTooLongSolving.add("o10932"); //  11.659s
//        filesTooLongSolving.add("o03097"); //  40.282s
//        filesTooLongSolving.add("o10991"); //  22.667s
//        filesTooLongSolving.add("o08676"); //  17.002s
//        filesTooLongSolving.add("o08360"); //  25.310s
//        filesTooLongSolving.add("o07594"); // 153.977s
//        filesTooLongSolving.add("o07810"); //  29.974s
//        filesTooLongSolving.add("o07501"); //  38.632s
//        filesTooLongSolving.add("o11427"); //  35.744s
//        filesTooLongSolving.add("o07833"); //  15.620s
//        filesTooLongSolving.add("o08222"); //  10.242s
//        filesTooLongSolving.add("o07343"); // 137.371s
//        filesTooLongSolving.add("o08026"); // 776.377s
//        filesTooLongSolving.add("o08123"); //1509.205s
//        filesTooLongSolving.add("o11542"); //  12.398s
//        filesTooLongSolving.add("o08493"); //  43.212s
//        filesTooLongSolving.add("o07978"); //  66.419s
//        filesTooLongSolving.add("o07406"); //  15.669s
//        filesTooLongSolving.add("o11601"); //  89.373s
//        filesTooLongSolving.add("o11630"); // 618.831s
//        filesTooLongSolving.add("o11513"); // 122.383s
//        filesTooLongSolving.add("o10659"); // 115.068s
//        filesTooLongSolving.add("o07860"); // 124.187s
//        filesTooLongSolving.add("o10070"); //  20.019s
//        filesTooLongSolving.add("o07276"); //  33.114s
//        filesTooLongSolving.add("o11798"); //  10.912s
//        filesTooLongSolving.add("o08346"); //  21.058s
//        filesTooLongSolving.add("o07495"); //  20.127s
//        filesTooLongSolving.add("o09828"); //  10.937s
//        filesTooLongSolving.add("o10851"); //  33.628s
//        filesTooLongSolving.add("o07402"); //  46.640s
//        filesTooLongSolving.add("o11177"); //2068.865s
//        filesTooLongSolving.add("o07985"); // 200.167s
//        filesTooLongSolving.add("o08748"); //  23.340s
//        filesTooLongSolving.add("o10170"); //  51.768s
//        filesTooLongSolving.add("o10153"); // 586.056s
//        filesTooLongSolving.add("o07499"); //  18.385s
//        filesTooLongSolving.add("o08315"); // 137.070s
//        filesTooLongSolving.add("o11430"); //  88.187s
//        filesTooLongSolving.add("o07483"); // 121.726s
//        filesTooLongSolving.add("o10589"); //  55.825s
//        filesTooLongSolving.add("o08421"); //  29.556s
//        filesTooLongSolving.add("o11463"); //  20.459s
//        filesTooLongSolving.add("o11375"); //  12.593s
//        filesTooLongSolving.add("o03300"); //  32.880s
//        filesTooLongSolving.add("o11375"); //  17.136s
//        filesTooLongSolving.add("o11375"); //  12.593s
//        filesTooLongSolving.add("o10573"); //  50.930s
//        filesTooLongSolving.add("o10493"); //  14.832s
//        filesTooLongSolving.add("o08365"); //  14.820s
//        filesTooLongSolving.add("o11141"); //  10.429s
//        filesTooLongSolving.add("o06607"); //1070.954s
//        filesTooLongSolving.add("o08197"); //  11.387s
//        filesTooLongSolving.add("o09940"); //  59.697s
//        filesTooLongSolving.add("o07160"); //  13.877s
//        filesTooLongSolving.add("o08139"); //  98.871s
//        filesTooLongSolving.add("o07839"); //  256.79s
//        filesTooLongSolving.add("o07160"); // 194.885s
//        filesTooLongSolving.add("o07423"); //  12.700s
//        filesTooLongSolving.add("o07160"); //  22.954s
//        filesTooLongSolving.add("o07332"); // 832.156s
//        filesTooLongSolving.add("o08547"); //  51.000s
//        filesTooLongSolving.add("o08050"); //  10.929s
//        filesTooLongSolving.add("o11511"); //  84.340s
//        filesTooLongSolving.add("o05747"); // 743.947s
//        filesTooLongSolving.add("o10637"); //  97.957s
//        filesTooLongSolving.add("o11460"); //  14.068s
//        filesTooLongSolving.add("o10922"); //  30.786s
//        filesTooLongSolving.add("o11704"); //  83.757s
//        filesTooLongSolving.add("o12324"); // 145.639s
//        filesTooLongSolving.add("o11425"); //  12.616s
//        filesTooLongSolving.add("o09995"); //  12.998s
//        filesTooLongSolving.add("o11457"); // 842.810s
//        filesTooLongSolving.add("o09090"); //  51.415s
//        filesTooLongSolving.add("o12313"); //  15.324s
//        filesTooLongSolving.add("o10306"); //  10.140s
//        filesTooLongSolving.add("o12099"); //  16.131s

        //Difficulty 5 logi:
//        filesTooLongSolving.add("o10310"); // 57s
//        filesTooLongSolving.add("o07502"); // 87s
//        filesTooLongSolving.add("o07518"); // 66s
//        filesTooLongSolving.add("o08298"); // długo nie wiem ile
//        filesTooLongSolving.add("o11803"); // ~62s
//        filesTooLongSolving.add("o07279"); // ~60s
//        filesTooLongSolving.add("o08623"); // 59s
//        filesTooLongSolving.add("o09378"); // 365s
//        filesTooLongSolving.add("o07412"); // ~550s
//        filesTooLongSolving.add("o10073"); // nie wiem ile
//        filesTooLongSolving.add("o11543"); // ~8min
//        filesTooLongSolving.add("o10686"); // ~5min
//        filesTooLongSolving.add("o07283"); // inf
//        filesTooLongSolving.add("o07396"); // inf
//        filesTooLongSolving.add("o11495"); // nie wiem ile
//        filesTooLongSolving.add("o08674"); // 63.5s
//        filesTooLongSolving.add("o10988"); // 52s
//        filesTooLongSolving.add("o11835"); // 68s
//        filesTooLongSolving.add("o11616"); // 55.3s
//        filesTooLongSolving.add("o09476"); // 219s
//        filesTooLongSolving.add("o07350"); // 192s
//        filesTooLongSolving.add("o08071"); // 1013s
//        filesTooLongSolving.add("o07486"); // 283s
//        filesTooLongSolving.add("o06941"); // 60s
//        filesTooLongSolving.add("o11964"); // 153s
//        filesTooLongSolving.add("o07961"); // 307s
//        filesTooLongSolving.add("o09471"); // 118s
//        filesTooLongSolving.add("o07803"); // 109s
//        filesTooLongSolving.add("o07460"); // 118s

        // katana non-solved heuristically in first version
        //15x15
//        filesTooLongSolving.add("Miś(Bear)(auth_aPeer)"); // 86s
//        filesTooLongSolving.add("Snejinka(auth_satt3047)"); // 67s
//        filesTooLongSolving.add("Pattern15x15_1(auth_wiki)"); // Recursion or sth need, Completion 0.89, 28s
//        filesTooLongSolving.add("Tak(auth_machina_virtualna)"); // 66s
//        filesTooLongSolving.add("Pattern(Patroon)(auth_Yayolo)"); // 52s
//        filesTooLongSolving.add("Wiatrak(Fan)(auth_Fromasz)"); // Recursion or sth need, Completion 0.89, 22s
//        filesTooLongSolving.add("Twirl(auth_Nillerdyret)"); // Recursion or sth need, Completion 40.89, 20s
//        filesTooLongSolving.add("Screaming_birdie(auth_Gam)"); // 106s
//        filesTooLongSolving.add("Diamond(auth_Amilida)"); // Recursion or sth need, Completion 21.33, 70s
//        filesTooLongSolving.add("Pattern(auth_lliyaa)"); // 25s
//        filesTooLongSolving.add("Carrelages(auth_Blinda)"); // Recursion or sth need, Completion 43.11, 23.593
//        filesTooLongSolving.add("Propeller(auth_Dobriy)"); //nullPointerException (empty solution)
//        filesTooLongSolving.add("Tea(auth_Les)"); // 32s
//        filesTooLongSolving.add("Flower(auth_Mirka3)"); // 59s
//        filesTooLongSolving.add("Earth_symbol(auth_Naar)"); // 43s
//        filesTooLongSolving.add("snowflake(auth_Arina2008Arina)"); // 31.917s
//        filesTooLongSolving.add("Pattern_37(auth_Ariannav)"); // 84s
//        filesTooLongSolving.add("Regression(auth_PrefrontalCortex)"); // Recursion or sth need, Completion 43.56, 30.858
//        filesTooLongSolving.add("Pig(auth_Mark321)"); // 82s
//        filesTooLongSolving.add("Spruce(auth_Murzik)"); // 166s (completion 100.0 - Recursion)
//        filesTooLongSolving.add("Heart(auth_beclyn)"); // Recursion or sth need, Completion 30.22, 50s
//        filesTooLongSolving.add("Pattern_2(auth_wim13)"); // Recursion or sth need, Completion 13.33, 91s
//        filesTooLongSolving.add("Magic_Staff(Staff)(auth_Twilia)"); // 31s
//        filesTooLongSolving.add("Christmas_Tree(auth_DejaV)"); // 65s
//        filesTooLongSolving.add("Cocker_Spaniel(auth_petunya)"); // 83s
//        filesTooLongSolving.add("My_weird_patterns_-_LII(auth_Psexanutik)"); // Recursion or sth need, Completion 1.78, 26s
//        filesTooLongSolving.add("Lilac(auth_Lisa80lvl)"); // 63s
//        filesTooLongSolving.add("Krtek(auth_Mirka3)"); // 28s
//        filesTooLongSolving.add("PGR(auth_Pegura)"); // 84s, 100% Completion (Recursion?)
//        filesTooLongSolving.add("(kanji)_fire(auth_Sonolumin)"); // 27.668s
//        filesTooLongSolving.add("(kanji)_fire(auth_Sonolumin)"); // 34s
//        filesTooLongSolving.add("Sleepy(auth_spock2009)"); // 92s
//        filesTooLongSolving.add("Farfalla(auth_ieia)"); // 107s, 100% Completion (completion 100.0 - Recursion)
//        filesTooLongSolving.add("X-Men(auth_Quodgephelph)"); // Recursion or sth need, Completion 5.33, 44.558s
//        filesTooLongSolving.add("sword_with_aura(auth_PPAPER)"); // 33s
//        filesTooLongSolving.add("My_weird_patterns_-_LVIII)(auth_Psexanutik)"); // 26.108s
//        filesTooLongSolving.add("Fancy_Crosshair(auth_Alexander_Aguirre)"); // 52s (completion 100.0 - Recursion)
//        filesTooLongSolving.add("Lilie(auth_FanLinkin)"); // 34s
//        filesTooLongSolving.add("Masterchief_helmet(Halo)(auth_Pate)"); // 45s
//        filesTooLongSolving.add("Bottle(auth_Scripty)"); // 79s, 100% Completion (completion 100.0 - Recursion)
//        filesTooLongSolving.add("X(auth_Tid88)"); //nullPointerException (empty solution)
//        filesTooLongSolving.add("Another_challenge_for_you(auth_Psexanutik)"); // 27s
//        filesTooLongSolving.add("Sword(auth_Darien_Fawkes)"); // 23s
//        filesTooLongSolving.add("Mouse(auth_Puzzhorn)"); // 26s
//        filesTooLongSolving.add("Wild_West(auth_Ed95206)"); // 24s
//        filesTooLongSolving.add("Escakeras(auth_El...)"); // 21s
//        filesTooLongSolving.add("Staircase(auth_Lullabeauxbug)"); // 29s
//        filesTooLongSolving.add("Pig(auth_Liuna)"); // 21s
//        filesTooLongSolving.add("Pattern15x15_2(auth_wiki)"); // 20s
//        filesTooLongSolving.add("Rocket(Auth_AlexPo03011975)"); // 19.9s
//        filesTooLongSolving.add("Pattern3(auth_OmaMor)"); // 28.199s
//        filesTooLongSolving.add("Baby_Stroller(Kingerwagen)(auth_Lostir)"); // 17.719s
//        filesTooLongSolving.add("Butterfly(Papillon)(auth_isiem)"); // 29.316s
//        filesTooLongSolving.add("Krest(auth_JannaS)"); // 50.398s
//        filesTooLongSolving.add("Pattern15x15_2(auth_KILK_UHA)"); // 32.59s
//        filesTooLongSolving.add("Vorobey(auth_Pechenka.)"); // 43.574s
//        filesTooLongSolving.add("Pattern(auth_Zettoo_Lei)"); // 28.654s
//        filesTooLongSolving.add("pattern(auth_Alexys12345)"); // 31.726s
//        filesTooLongSolving.add("Dying_bush(auth_Kateivas)"); // 31.648s
//        filesTooLongSolving.add("Targeting_computer(auth_m.junior)"); // Recursion or sth need, Completion 18.22, 59.171s
//        filesTooLongSolving.add("ghost(auth_anna3337777)"); // 62.438s
//        filesTooLongSolving.add("Scissors(Schere)(auth_Zarathustra)"); // Recursion or sth need, Completion 12.44, 71.446s
//        filesTooLongSolving.add("Flower(auth_Rain)"); // 42.792s
//        filesTooLongSolving.add("Curl(auth_mystery_soul)"); // 139.058s
//        filesTooLongSolving.add("Scissors(Schere)(auth_Zarathustra)"); // Recursion or sth need, Completion 14.67, 69.792s
//        filesTooLongSolving.add("pattern_1(auth_Erfina)"); // Recursion or sth need, Completion 23.11, 73.195s
//        filesTooLongSolving.add("Bracelet(auth_DrewSamson)"); // 39.554s
//        filesTooLongSolving.add("Chick(auth_keikyu2100gata)"); // 28.858s
//        filesTooLongSolving.add("Uzor(auth_Shurik702)"); // 29.993s
//        filesTooLongSolving.add("The_allseeing_eye(auth_Donovan)"); // 48.193s
//        filesTooLongSolving.add("Butterfly(Mariposa)(auth_zupermami)"); // 35.591s
//        filesTooLongSolving.add("Goat(auth_MissMijo)"); // 54.377s
//        filesTooLongSolving.add("Pattern_7(auth_Logard)"); //nullPointerException
//        filesTooLongSolving.add("Stamp(Estampa)(auth_Nunovni)"); // Recursion or sth need, Completion 14.67, 83.473s
//        filesTooLongSolving.add("Desert_Highway(auth_BlindWanderer)"); // Recursion or sth need, Completion 2.67, 55.287s
//        filesTooLongSolving.add("Snowflake(auth_Evgenyi)"); // 65s, 100% Completion (completion 100.0 - Recursion)
//        filesTooLongSolving.add("flower(auth_snowyowl8)"); // Recursion or sth need, Completion 25.33, 111.076s
//        filesTooLongSolving.add("Pattern(auth_Yennefer17)"); // Recursion or sth need, Completion 43.11, 54.499s
//        filesTooLongSolving.add("Web(auth_Vitaliy_Lokos)"); // 26.45s
//        filesTooLongSolving.add("Enda(auth_naaaa)"); // 41.393s
//        filesTooLongSolving.add("Feather(auth_Annsan)"); // 98s, 100% Completion (completion 100.0 - Recursion)
//        filesTooLongSolving.add("Stripes(auth_Mag&3)"); // 26.032s
//        filesTooLongSolving.add("Argentine_Mate(Mate_Argentino)(auth_Alud)"); // 20.396s
//        filesTooLongSolving.add("Little_bird(P1)(auth_charly65)"); // 52.654s
//        filesTooLongSolving.add("Pattern(auth_KILK_UHA)"); // Recursion or sth need, Completion 40.89, 167.889s
//        filesTooLongSolving.add("Clover(auth_cyndidee)"); // 93s, 100% Completion (Recursion?)
        //20x20                                                                                    up to 5 recursion depth (sC = 157, sP = 89.2)
        //filesTooLongSolving.add("Doggy(Chiot)(auth_Liaaaaaaaaaa)");                                 //    2.054s 100.00%
        //filesTooLongSolving.add("Halberd(auth_Aurelius)");                                          //    2.321s 100.00%
        filesTooLongSolving.add("Doily(auth_Ricarix)");                                             //  141.034s   0.00%
        //filesTooLongSolving.add("White_cross(auth_Sanchez_Solver)");                                //    0.605s   0.00%
        filesTooLongSolving.add("Knight(auth_DrTimer)");                                            //   84.636s 100.00%
        filesTooLongSolving.add("Pocket_bouquet(auth_inush)");                                      //  184.109s 100.00%
        filesTooLongSolving.add("Links(auth_Ricarix)");                                             //   69.375s 100.00%
        filesTooLongSolving.add("Geo_Abstract(auth_Turfer)");                                       //    1.585s 100.00%
        filesTooLongSolving.add("Rune(auth_Mihail102)");                                            //  348.304s  20.00%
        filesTooLongSolving.add("Owl(auth_Yanire)");                                                //    1.858s 100.00%

        filesTooLongSolving.add("Little_star(auth_Force_of_Nature)");                               //  467.579s   8.75%
        filesTooLongSolving.add("Bumpy_1989_y.(Game)(auth_Milana.)");                               //   23.429s 100.00%
        filesTooLongSolving.add("Sword(auth_Izuri_Natsuki)");                                       //  117.306s 100.00%
        filesTooLongSolving.add("Entrance(auth_The_Dimmon0811)");                                   //   13.318s 100.00%
        filesTooLongSolving.add("Island(auth_Lexruss)");                                            //  141.846s 100.00% /recDepth=1
        filesTooLongSolving.add("Baymax(auth_chopper)");                                            //  285.787s 100.00% /recDepth=1
        filesTooLongSolving.add("Simmetria3(auth_mrs.zenzy)");                                      //    4.645s 100.00%
        filesTooLongSolving.add("Ficus_elastica(Gummibaum)(auth_FairyWings29)");                    //   32.275s 100.00%
        filesTooLongSolving.add("Pedestal(auth_igormart)");                                         //   26.940s 100.00%
        filesTooLongSolving.add("Puzzle_2(auth_Eccentric)");                                        //    6.221s 100.00%

        filesTooLongSolving.add("Muster(auth_lisa&)");                                              //   15.198s 100.00%
        filesTooLongSolving.add("jellyfish(auth_dydh1)");                                           //   86.415s 100.00%
        filesTooLongSolving.add("Spirale(auth_Whitiger13)");                                        //   45.266s 100.00%
        filesTooLongSolving.add("Pattern_9(auth_wim13)");                                           //    5.175s 100.00%
        filesTooLongSolving.add("Gecko(auth_Neeky)");                                               //   56.052s 100.00%
        filesTooLongSolving.add("Uzory_Taimyra_3(auth_untaika)");                                   //  135.863s  48.50%
        filesTooLongSolving.add("Squares(auth_bakagiggio)");                                        //  135.042s 100.00%
        filesTooLongSolving.add("Crabe(auth_D93)");                                                 //   82.003s 100.00%
        filesTooLongSolving.add("Teddy_Bear(auth_dizziness)");                                      //  109.376s 100.00%
        filesTooLongSolving.add("Trumpet(auth_sydmoney42)");                                        //   64.994s 100.00%

        filesTooLongSolving.add("Screw(auth_Feiry)");                                               //  125.888s 100.00% /recDepth=3
        filesTooLongSolving.add("Nothing(auth_mijo)");                                              //  192.251s  52.00%
        filesTooLongSolving.add("Samurai(auth_Nomad)");                                             //  112.158s 100.00%
        filesTooLongSolving.add("wintertree(winterboom)(auth_sterredag)");                          //   78.469s 100.00%
        filesTooLongSolving.add("Roza_vetrov(auth_Dittodendron)");                                  //   31.291s 100.00%
        filesTooLongSolving.add("Square(hard_for_computer_easy_for_human)(auth_Gcpsu)");            //  479.165s  11.75%
        filesTooLongSolving.add("Snowman(auth_Ladyweed)");                                          //  408.659s 100.00%
        filesTooLongSolving.add("Mini_evil(auth_Mefis)");                                           //   94.979s 100.00%
        filesTooLongSolving.add("Needle_and_Button(auth_Swapnil_Bankar)");                          //   95.942s 100.00%
        filesTooLongSolving.add("Mozaika(auth_ka_ha)");                                             //    0.151s 100.00%

        filesTooLongSolving.add("Insect(Insetto)(auth_mrs.zenzy)");                                 //  143.369s 100.00%
        filesTooLongSolving.add("X-MAS_TREE(Kerstboom)(auth_Dientje85)");                           //   96.134s 100.00%
        filesTooLongSolving.add("Circle(Cirkel)(auth_Bro94)");                                      //   23.279s 100.00%
        filesTooLongSolving.add("Tarcza(auth_tabaq)");                                              //    0.762s 100.00%
        filesTooLongSolving.add("Clock(auth_rasolinenet1988)");                                     //  329.324s 100.00% /recDepth=4
        filesTooLongSolving.add("Chain_Link(auth_BlueRolex)");                                      //  128.712s 100.00%
        filesTooLongSolving.add("Mosaic(auth_eil5026)");                                            //   63.765s  48.00%
        filesTooLongSolving.add("4(Sword)(auth_XXIxDUDxIXX))");                                     //   85.564s 100.00%
        filesTooLongSolving.add("Cross(auth_KILK_UHA))");                                           //  253.694s   0.00%
        filesTooLongSolving.add("Piggy(Schweinchen)(auth_Flauschel)");                              //  215.071s 100.00%

        filesTooLongSolving.add("Danseurs(auth_louiis)");                                           //   18.165s 100.00%
        filesTooLongSolving.add("Note_key(auth_Marryka11)");                                        //   14.700s 100.00%
        filesTooLongSolving.add("Forest(Wald)(auth_Arinome)");                                      //   11.255s 100.00%
        filesTooLongSolving.add("Horse(auth_Avtogragdanka)");                                       //   55.733s 100.00%
        filesTooLongSolving.add("Birdbox(Caja_nido)(auth_Caminero)");                               //  138.864s 100.00% /recDepth=1
        filesTooLongSolving.add("Knit(Tricot)(auth_gennao)");                                       //    6.314s 100.00%
        filesTooLongSolving.add("Battle_hammer(auth_money_D_luffy)");                               //   11.260s 100.00%
        filesTooLongSolving.add("The_King(auth_Blizzard)");                                         //   27.191s 100.00%
        filesTooLongSolving.add("Vase(auth_kronki)");                                               //  122.524s 100.00%
        filesTooLongSolving.add("Symetry(Simetria)(auth_rockejr)");                                 //  125.720s 100.00%

        filesTooLongSolving.add("Rabbit(auth_Victor_304)");                                         //   96.810s 100.00%
        filesTooLongSolving.add("Pattern20x20_1(auth_wiki)");                                       //   19.347s 100.00%
        filesTooLongSolving.add("Fish(auth_Santalina)");                                            //  218.802s   5.50%
        filesTooLongSolving.add("Kitty(auth_Gragdanochka)");                                        //   64.303s 100.00%
        filesTooLongSolving.add("Sym-design(auth_Ankit_Seth))");                                    //    1.270s 100.00%
        filesTooLongSolving.add("Soccer_player(auth_Utsu)");                                        //   21.438s 100.00%
        filesTooLongSolving.add("The_world_is_one_family(Vasudaiva_kutumbakam)(auth_Aditya_Deshmukh)");// 6.187s 100.00%
        filesTooLongSolving.add("Mt._FUJI(auth_kamegon)");                                          //    9.418s 100.00%
        filesTooLongSolving.add("Squirrel(auth_Trofimka2210)");                                     //  422.999s 100.00% /recDepth=5
        filesTooLongSolving.add("In_a_Heartbeat(auth_Smartypants)");                                //  114.470s 100.00% /recDepth=2

        filesTooLongSolving.add("Love(auth_Callie_Cassidy)");                                       //  147.555s 100.00%
        filesTooLongSolving.add("Squirtle(auth_Scarheart)");                                        //   47.638s 100.00%
        filesTooLongSolving.add("Chertik(auth_Natik2002)");                                         //  212.890s 100.00%
        filesTooLongSolving.add("Simmetria9(auth_mrs.zenzy))");                                     //    8.551s 100.00%
        filesTooLongSolving.add("Flying_bird(auth_Ladyweed)");                                      //  254.866s   2.50%
        filesTooLongSolving.add("Flower20x20(auth_eil5026)");                                       //   24.190s 100.00%
        filesTooLongSolving.add("Parrot(auth_Natali_san)");                                         //   25.262s 100.00%
        filesTooLongSolving.add("Morkovka(auth_Yaroslav_Pidgurskyi)");                              //    2.079s 100.00%
        filesTooLongSolving.add("Simmetria15(auth_mrs.zenzy)");                                     //  140.798s 100.00% /recDepth=2
        filesTooLongSolving.add("Flower(auth_eil5026)");                                            //    0.304s 100.00%

        filesTooLongSolving.add("Pattern_66(auth_Kleopatra)");                                      //    6.495s 100.00%
        filesTooLongSolving.add("Ghost(auth_Kyte)");                                                //  245.687s 100.00% /recDepth=1
        filesTooLongSolving.add("Native_American(Indianer)(auth_gojira)");                          //  181.139s 100.00%
        filesTooLongSolving.add("Smile(auth_whenwolf)");                                            //   40.562s 100.00%
        filesTooLongSolving.add("Fish(auth_IrinaYa)");                                              //    4.577s 100.00%
        filesTooLongSolving.add("Girl(auth_Yucia)");                                                //    0.391s 100.00%
        filesTooLongSolving.add("KEngine_piston(Pistone,_fascia_e_perno)(auth_Sigmundd)");          //  134.346s 100.00%
        filesTooLongSolving.add("KGungnir(auth_Super_hitman)");                                     //    0.447s 100.00%
        filesTooLongSolving.add("Sleeping_cat(auth_Juli-ma)");                                      //   56.502s 100.00%
        filesTooLongSolving.add("Tri(auth_st3fn0)");                                                //  226.002s  16.50%

        filesTooLongSolving.add("Znak_drogowy(auth_an84na)");                                       //   84.176s 100.00%
        filesTooLongSolving.add("Flying_monster(walk)(auth_Gipnoza)");                              //   79.200s 100.00%
        filesTooLongSolving.add("Llama_Love(auth_SunKissed)");                                      //   19.515s 100.00%
        filesTooLongSolving.add("napkin(auth_IMITATOR)");                                           //    2.381s 100.00%
        filesTooLongSolving.add("Hmmmph(auth_QuinnO)");                                             //   63.739s 100.00%
        filesTooLongSolving.add("1+1(auth_Russell2012)");                                           //  222.073s 100.00%
        filesTooLongSolving.add("Pattern4(auth_wiki)");                                             //  263.397s 100.00% /recDepth=5
        filesTooLongSolving.add("Fingerwaves(Onde_Giapponesi)(auth_Fusettini)");                    //   19.384s 100.00%
        filesTooLongSolving.add("Kanji_for_fire(huo3)(auth_ai2022ncg)");                            //  148.163s 100.00%
        filesTooLongSolving.add("Palm(auth_mmaga)");                                                //    0.975s 100.00%

        filesTooLongSolving.add("Celtic_cross(auth_Zipfish)");                                      //   92.224s 100.00% /recDepth=1
        filesTooLongSolving.add("Mozaic_1(auth_wim13)");                                            //    1.570s 100.00%
        filesTooLongSolving.add("Pattern20x20(auth_wiki)");                                         //  465.870s  21.00%
        filesTooLongSolving.add("Curious_monkey(Neugiriger_Affe)(Auth_Fuzzly)");                    //   55.250s 100.00%
        filesTooLongSolving.add("Heart(auth_Svar)");                                                //   35.216s 100.00%
        filesTooLongSolving.add("Jellyfish(auth_el1s3n)");                                          //   12.962s 100.00%
        filesTooLongSolving.add("Kaleidoscope(Kaleidoskop)(auth_MarStav)");                         //   71.450s 100.00%
        filesTooLongSolving.add("Gold(auth_STK)");                                                  //   14.343s 100.00%
        filesTooLongSolving.add("baseball(auth_Devikajoy)");                                        //  255.194s  27.00%
        filesTooLongSolving.add("Don't_know(auth_Y88n_)");                                          //    0.478s 100.00%

        filesTooLongSolving.add("Kat(auth_Happy__Girll)");                                          //   33.886s 100.00%
        filesTooLongSolving.add("Cute_dog(auth_Hawka)");                                            //  194.954s 100.00%
        filesTooLongSolving.add("Sunset(auth_pearlrose)");                                          //  116.217s 100.00%
        filesTooLongSolving.add("Bow_and_arrow(auth_Ankit_Seth)");                                  //  171.357s 100.00%
        filesTooLongSolving.add("Fist(auth_Alturo)");                                               //    3.480s 100.00%
        filesTooLongSolving.add("Pick_axe(auth_Kitticats)");                                        //   80.823s 100.00%
        filesTooLongSolving.add("Flower_on_a_vase(Flor_no_vaso)(auth_Marilha)");                    //  577.847s  31.50%
        filesTooLongSolving.add("House(Maison)(auth_Shusy)");                                       //  116.657s 100.00%
        filesTooLongSolving.add("Baseball(auth_Doctor_J)");                                         //   84.049s 100.00%
        filesTooLongSolving.add("Horse(auth_Caballo)");                                             //  262.745s 100.00%

        filesTooLongSolving.add("wifi(auth_mana)");                                                 //    0.826s 100.00%
        filesTooLongSolving.add("Rebellion(auth_Spiderlux)");                                       //    0.268s 100.00%
        filesTooLongSolving.add("Whack-a-mole(Acchiappa_la_talpa)(auth_XCloud92)");                 //   59.950s 100.00%
        filesTooLongSolving.add("pattern(auth_jh1318)");                                            //    1.602s 100.00%
        filesTooLongSolving.add("Pisces(auth_boobie420)");                                          //  185.992s 100.00%
        filesTooLongSolving.add("Kilroy_Was_Here(auth_TK421)");                                     //    5.348s 100.00%
        filesTooLongSolving.add("Cap's_Shield(Lo_scudo_del_Capitano)(auth_PC37)");                  //   65.433s 100.00%
        filesTooLongSolving.add("Key(auth_dr_pure)");                                               //   30.252s 100.00%
        filesTooLongSolving.add("Dagger(auth_PPAPER)");                                             //  119.122s 100.00%
        filesTooLongSolving.add("Cubes(auth_NDee)");                                                // 1074.434s 100.00% /recDepth=5

        filesTooLongSolving.add("Clown(auth_chopper)");                                             //    0.253s 100.00%
        filesTooLongSolving.add("Jellyfish(auth_byiguana)");                                        //   13.983s 100.00%
        filesTooLongSolving.add("Sword(auth_TAGVoar)");                                             //  131.329s 100.00%
        filesTooLongSolving.add("Coal_Waggon(auth_Triple_S)");                                      //    0.251s 100.00%
        filesTooLongSolving.add("Sword(auth_Vojta_Vavrik)");                                        //  263.962s 100.00%
        filesTooLongSolving.add("Taurus(auth_GalyaAs)");                                            //    2.836s 100.00%
        filesTooLongSolving.add("Rosa_dos_ventos(auth_Desinho)");                                   //  262.412s 100.00%
        filesTooLongSolving.add("Star(auth_Aleris)");                                               //  145.601s   0.00%
        filesTooLongSolving.add("Design(auth_tkocer17)");                                           //   62.532s 100.00%
        filesTooLongSolving.add("Rose(auth_iStudent)");                                             //   83.276s 100.00%

        filesTooLongSolving.add("Ovni(auth_Porra)");                                                //  115.971s 100.00% /recDepth=3
        filesTooLongSolving.add("Design_2(auth_SunKissed)");                                        //  391.164s 34.00%
        filesTooLongSolving.add("Bear(auth_Murzik)");                                               //   13.181s 100.00%
        filesTooLongSolving.add("Butterfly(auth_masha.ff)");                                        //   93.701s 100.00%
        filesTooLongSolving.add("Which_line_is_longest(auth_Samlls)");                              //  231.530s  19.50%
        filesTooLongSolving.add("Star(Stern)(auth_sisika)");                                        //    4.361s 100.00%
        filesTooLongSolving.add("Sword(auth_AnkebuT35)");                                           //  206.525s   0.50%
        filesTooLongSolving.add("Cat(auth_Nec)");                                                   //  134.165s 100.00%
        filesTooLongSolving.add("illusion(auth_efam)");                                             //  124.629s 100.00%
        filesTooLongSolving.add("Pattern(auth_greenmusic)");                                        //    0.774s 100.00%

        filesTooLongSolving.add("Ddd(auth_bro_ry)");                                                //   37.064s 100.00%
        filesTooLongSolving.add("A37(auth_Ulti)");                                                  //  141.072s 100.00%
        filesTooLongSolving.add("Pattern_8(auth_wim13)");                                           //    1.879s 100.00%
        filesTooLongSolving.add("Cat_in_the_sunshine(auth_aaa1)");                                  //  116.666s 100.00%
        filesTooLongSolving.add("Gothic_cat(auth_Auf228)");                                         //  256.059s 100.00%
        filesTooLongSolving.add("Duck(auth_Yurik)");                                                // 1447.235s 100.00%
        filesTooLongSolving.add("Coffee_jug(auth_Ricarix)");                                        //    0.774s  27.00%
        filesTooLongSolving.add("Pattern_91(auth_Irene_009)");                                      //    8.857s 100.00%
        filesTooLongSolving.add("Stsr(auth_Igaryok)");                                              //   25.521s 100.00%
        filesTooLongSolving.add("Wisdom_knot(Nyansapo)(auth_AhmetBayirli)");                        //   82.738s 100.00%

        filesTooLongSolving.add("Ddd(auth_bro_ry)");                                                //   37.064s 100.00%
        filesTooLongSolving.add("A37(auth_Ulti)");                                                  //  141.072s 100.00%
        filesTooLongSolving.add("Pattern_8(auth_wim13)");                                           //    1.879s 100.00%
        filesTooLongSolving.add("Cat_in_the_sunshine(auth_aaa1)");                                  //  116.666s 100.00%
        filesTooLongSolving.add("Gothic_cat(auth_Auf228)");                                         //  256.059s 100.00%
        filesTooLongSolving.add("Duck(auth_Yurik)");                                                // 1447.235s 100.00%
        filesTooLongSolving.add("Coffee_jug(auth_Ricarix)");                                        //    0.774s  27.00%
        filesTooLongSolving.add("Pattern_91(auth_Irene_009)");                                      //    8.857s 100.00%
        filesTooLongSolving.add("Stsr(auth_Igaryok)");                                              //   25.521s 100.00%
        filesTooLongSolving.add("Wisdom_knot(Nyansapo)(auth_AhmetBayirli)");                        //   82.738s 100.00%

        filesTooLongSolving.add("Ddd(auth_bro_ry)");                                                //   37.064s 100.00%
        filesTooLongSolving.add("A37(auth_Ulti)");                                                  //  141.072s 100.00%
        filesTooLongSolving.add("Pattern_8(auth_wim13)");                                           //    1.879s 100.00%
        filesTooLongSolving.add("Cat_in_the_sunshine(auth_aaa1)");                                  //  116.666s 100.00%
        filesTooLongSolving.add("Gothic_cat(auth_Auf228)");                                         //  256.059s 100.00%
        filesTooLongSolving.add("Duck(auth_Yurik)");                                                // 1447.235s 100.00%
        filesTooLongSolving.add("Coffee_jug(auth_Ricarix)");                                        //    0.774s  27.00%
        filesTooLongSolving.add("Pattern_91(auth_Irene_009)");                                      //    8.857s 100.00%
        filesTooLongSolving.add("Stsr(auth_Igaryok)");                                              //   25.521s 100.00%
        filesTooLongSolving.add("Wisdom_knot(Nyansapo)(auth_AhmetBayirli)");                        //   82.738s 100.00%

        filesTooLongSolving.add("dandelion(auth_Gragdanochka)");                                    //    2.295s 100.00%
        filesTooLongSolving.add("King(auth_DrTimer)");                                              //   38.169s 100.00%
        filesTooLongSolving.add("Cubo(auth_Jasson_Manuel)");                                        //   12.138s 100.00%
        filesTooLongSolving.add("Eye_monster(auth_David_M)");                                       //  216.687s 100.00%
        filesTooLongSolving.add("Snail(Schnecke)(auth_bandenklette)");                              //  447.348s 100.00%
        filesTooLongSolving.add("Flower(Fleur)(auth_cyn86)");                                       //   88.500s 100.00%
        filesTooLongSolving.add("Boat(auth_Thomas_van_Driel)");                                     //    6.846s  27.00%
        filesTooLongSolving.add("Branch(auth_Arahnia)");                                            //    2.357s 100.00%
        filesTooLongSolving.add("Design_01(auth_Nancy_McCrary)");                                   //  338.059s 100.00%
        filesTooLongSolving.add("Good_boy(auth_SayHi)");                                            //   97.466s 100.00%

        filesTooLongSolving.add("Star(auth_Ulti)");                                                 //  133.921s 100.00% /recDepth=2
        filesTooLongSolving.add("Cats(auth_blisster)");                                             //   89.726s 100.00%
        filesTooLongSolving.add("Foot(Planta_del_pie)(auth_Ladyweed)");                             //  635.799s  27.25%
        filesTooLongSolving.add("Optical_pattern(auth_Purr_Norris)");                               //   97.278s 100.00% /recDepth=5
        filesTooLongSolving.add("Snail(Schnecke)(auth_bandenklette)");                              //  245.366s 100.00%
        filesTooLongSolving.add("Tiles(auth_Miss_Annasita)");                                       //    3.102s 100.00%
        filesTooLongSolving.add("Kite(auth_Tatyana30)");                                            //   49.206s 100.00%
        filesTooLongSolving.add("Graveyard(auth_Engorged)");                                        //   94.294s 100.00%
        filesTooLongSolving.add("bismuth_patterns(auth_masekre)");                                  //  101.410s 100.00%
        filesTooLongSolving.add("Forks_4(auth_Arik_Manley)");                                       //   14.737s 100.00%

        filesTooLongSolving.add("Forks_2(auth_Arik_Manley)");                                       //   59.097s 100.00% /recDepth=2
        filesTooLongSolving.add("floral_tile(auth_bizibody)");                                      //  481.528s 100.00% /recDepth=2
        filesTooLongSolving.add("Pattern_10_Hard(gypsyfyed333(pub)(auth_Kimberly_Edens)");          //    1.602s 100.00%

        //25x25 (results with maxTreeHeight == 1)

        System.out.println("Selected nonograms count: " + selectedCount);

        NonogramFileDetails nonogramFileDetails;
        NonogramLogic nonogramLogicToSolve;
        NonogramLogic nonogramLogicSolved;

        int nonogramNo = 1;


        for (Nonogram nonogram : selectedNonogramsList) {
            difficulty = nonogram.getDifficulty();
            filename = nonogram.getFilename();
            height = nonogram.getHeight();
            month = nonogram.getMonth();
            source = nonogram.getSource();
            width = nonogram.getWidth();
            year = nonogram.getYear();

            //System.out.print('"' + filename + '"' + " " );

            ObjectMapper objectMapper = new ObjectMapper();
            nonogramFileDetails = objectMapper.readValue(
                    new File(puzzlePath + filename + JSON_EXTENSION), NonogramFileDetails.class
            );

            nonogramLogicToSolve = new NonogramLogic(
                    nonogramFileDetails.getRowSequences(), nonogramFileDetails.getColumnSequences(), false);

            if(!filesTooLongSolving.contains(filename)) {
                long start = System.currentTimeMillis();
                nonogramLogicSolved = nonogramLogicService.runSolverWithCorrectnessCheck(nonogramLogicToSolve,
                        filename + JSON_EXTENSION);
                long finish = System.currentTimeMillis();
                long timeElapsed = finish - start;
                double secondsElapsed = (double) timeElapsed / 1000.0;

                System.out.println(secondsElapsed + "s " + nonogramLogicSolved.getCompletionPercentage() + "%");

                if(nonogramLogicSolved.getCompletionPercentage() == 100) {
                    solvedCount = solvedCount + 1;
                    if(saveSolutions) {
                        nonogramService.saveSolutionToFile(filename, nonogramLogicSolved);
                    }
                }
                selectedCount = selectedCount + 1;
            }

            nonogramNo = nonogramNo + 1;
        }

        System.out.println("Solved count: " + solvedCount);
        double percentageSolved = Math.round(((double)(solvedCount) / selectedCount) * 10000 ) / 100.0;
        System.out.println("Percentage solved: " +  percentageSolved);
    }
}