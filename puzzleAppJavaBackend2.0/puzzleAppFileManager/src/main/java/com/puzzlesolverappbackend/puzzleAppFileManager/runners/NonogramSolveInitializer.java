package com.puzzlesolverappbackend.puzzleAppFileManager.runners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puzzlesolverappbackend.puzzleAppFileManager.NonogramSolutionDecision;
import com.puzzlesolverappbackend.puzzleAppFileManager.config.DecisionMode;
import com.puzzlesolverappbackend.puzzleAppFileManager.config.RecursiveMode;
import com.puzzlesolverappbackend.puzzleAppFileManager.model.Nonogram;
import com.puzzlesolverappbackend.puzzleAppFileManager.model.NonogramResult;
import com.puzzlesolverappbackend.puzzleAppFileManager.payload.NonogramFileDetails;
import com.puzzlesolverappbackend.puzzleAppFileManager.payload.NonogramLogic;
import com.puzzlesolverappbackend.puzzleAppFileManager.repository.NonogramRepository;
import com.puzzlesolverappbackend.puzzleAppFileManager.services.CommonService;
import com.puzzlesolverappbackend.puzzleAppFileManager.services.NonogramLogicService;
import com.puzzlesolverappbackend.puzzleAppFileManager.services.NonogramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.*;

import static com.puzzlesolverappbackend.puzzleAppFileManager.services.NonogramLogicService.printHeaders;

//@Component
//@Order(7)
public class NonogramSolveInitializer implements CommandLineRunner {

    boolean saveSolutions = true;

    String sourceQuery = "katana";
    ObjectMapper objectMapper;

    List<Nonogram> selectedNonogramsList;
    @Autowired
    private NonogramRepository nonogramRepository;

    @Autowired
    NonogramLogicService nonogramLogicService;

    @Autowired
    NonogramService nonogramService;

    @Autowired
    CommonService commonService;

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

    @Override
    public void run(String... args) throws Exception {

        init();

        int selectedCount = 0;
        int solvedCount = 0;

        List<String> filesTooLongSolving = new ArrayList<>();
        List<String> filesToSolve = new ArrayList<>(Arrays.asList("Clock(auth_corvin_mma)"));
//        List<String> filesToSolve = new ArrayList<>(
//                Arrays.asList("Salfetka_s_uzorom(auth_Yurok1159)",
//                        "Positive_&_Negative(Mini_Relaxation_-_BW19)(auth_Mushroom-World)",
//                        "Android(auth_Warior_Bushido)",
//                        "Art3(Kunst3)(auth_TanteM)",
//                        "Checkerboard_cube(auth_Emron)",
//                        "Pattern(Patrón)(auth_Minilla)",
//                        "I_m_hungry(auth_cecelest1)",
//                        "Cireus(auth_Iraida)",
//                        "Hatull!(auth_Roey)",
//                        "Pattern_1(auth_hellersjoke)",
//                        "Evolution(auth_RedTrombPictures)",
//                        "Beach(Playa)(auth_Anaid)",
//                        "Pattern_155(auth_Irene_009)",
//                        "Design_11(auth_Nancy_McCrary)",
//                        "Muster(auth_ChrissyFeldy)",
//                        "Clock(auth_corvin_mma)",
//                        "Bottom(auth_don_Pecco)",
//                        "Pattern_84(Irene_009)",
//                        "Sunchild(auth_iltarusko)",
//                        "Positive_&_Negative(Mini_Relaxation_-_BW_15)",
//                        "Pattern_73(auth_Irene_009)",
//                        "Invisible(auth_mily75)",
//                        "królik(rabbit)(auth_cyanida)",
//                        "Mandala_10(auth_Souldasher)",
//                        "Ornament(auth_Slava91)",
//                        "Flowers(auth_Ursuscraft)",
//                        "Santa(Mr_Claus)(auth_Nancy_McCrary)",
//                        "Pattern_272(auth_Irene_009)",
//                        "Eye_of_Horus(auth_Carlos_M)",
//                        "Traditional_Pattern(auth_Mooloolaba)",
//                        "Love(auth_Antonina93)",
//                        "Simmetria25(auth_mrs.zenzy)",
//                        "Funny_character(Pamperek)(auth_morrigannn)",
//                        "Indian_pattern(auth_Carola1976)",
//                        "Looked_Right_At_It,_Idiot(auth_sauce_boss)",
//                        "Home(auth_Fearless)",
//                        "Pięść_walki(Fight_fist)(auth_blackphoenix)",
//                        "Drakon(auth_Ekaterinakluch)",
//                        "Celtic_Square(auth_Ryuzaki_Hirokai)",
//                        "Eye(auth_skyemoon)",
//                        "Cafeteria(Caffettiera)(auth_Sigmundd)",
//                        "Pattern004(Corak004)(auth_Orihime_Inoue)",
//                        "A_hand(auth_OqueMorrin)",
//                        "Maze3(auth_Belore)",
//                        "dog(auth_marke413)",
//                        "Anarchy(auth_margotte)",
//                        "Doraemon(auth_locy)",
//                        "Uzor(auth_iljuham)",
//                        "Sunchild(auth_iltarusko)",
//                        "Tree(auth_AztekPlay)",
//                        "Positive_&_Negative_22(auth_Mushroom-World)",
//                        "All_in_father(auth_Bag131)",
//                        "Make_up(auth_jillybean)",
//                        "High_hopes(auth_B4L4NC3)",
//                        "Minotaturus_of_labylinth(auth_Zionlee)",
//                        "Dragon_head(auth_azkaila)",
//                        "Blot(auth_CKPEIIKA)",
//                        "Whale(auth_GiftieEtcetera)",
//                        "Yin_yang_snakes(auth_The_Coatman)",
//                        "Fantasy_Owl(Abstrakte_Eule)(auth_littlebee)",
//                        "Pattern_78(auth_Irene_009)",
//                        "Plume(auth_crosscrosslou)",
//                        "Sun_Eye(auth_Basilisk)",
//                        "Dart(auth_Daniel_Bury)",
//                        "Snowflake(auth_Inese)",
//                        "Pattern_270(auth_Irene_009)",
//                        "Boat(auth_Maga)",
//                        "mountain_creek(auth_darth_pixel)",
//                        "Spider(auth_Nekij_San)",
//                        "Geometric(auth_sarahcath)",
//                        "Phantom(auth_CharlesKKM)",
//                        "Kino(auth_maksxxx555)",
//                        "Celestia(auth_IDraco)",
//                        "Bread(auth_TiaLight)",
//                        "Pythagorus_Theorem(auth_crazyEco)",
//                        "Accordeon(Akkordeon)(auth_FloraAndJasper)",
//                        "Atom(auth_TheChemist)",
//                        "Aquarium(auth_michel45200)",
//                        "Foxes_are_afraid_of_future4(auth_kakashka07)",
//                        "Home(auth_cchrissy)",
//                        "Leontopdium(auth_Lvovna)",
//                        "Katekoi_hitman_Reborn(auth_DokterPanda)",
//                        "smiling_face(Lachendes_Gesicht)",
//                        "Maze_Design(auth_wiki)",
//                        "Simple_Mandala(auth_netta)",
//                        "Pattern_314_a(auth_Irene_009)",
//                        "Hello(auth_MM2003)",
//                        "China_rose,_hibiscus(auth_Lukefoo)",
//                        "Mountains(auth_Squirtle5)",
//                        "Dragonfly(auth_Nancy_McCrary)",
//                        "Pattern(auth_wiki)30x30",
//                        "Optical_Illusion_Diagonals_Parallel(auth_ErinAero1)",
//                        "Rooster(Kohout)(auth_FinalArgument)"));

        System.out.println("Selected nonograms count: " + selectedCount);

        NonogramFileDetails nonogramFileDetails;
        NonogramLogic nonogramLogicToSolve;
        NonogramLogic nonogramLogicSolved;

        int nonogramNo = 1;
        int loopsCount = 1;

        printHeaders();

            for (Nonogram nonogram : selectedNonogramsList) {
                extractValuesFromNonogram(nonogram);

                nonogramFileDetails = objectMapper.readValue(
                        new File(puzzlePath + filename + ".json"), NonogramFileDetails.class
                );

                nonogramLogicToSolve = new NonogramLogic(
                        nonogramFileDetails.getRowSequences(), nonogramFileDetails.getColumnSequences());

                nonogramLogicService.changeConfig(DecisionMode.RANDOM, RecursiveMode.RECURSION_MAX_FROM_BOTH_NODES);

                System.out.println("filename: " + filename);
                if(filesToSolve.contains(filename)) {
                    System.out.println("file to solve: " + filename);
                    nonogramLogicSolved = nonogramLogicService.runCustomSolverOperationWithCorrectnessCheck(nonogramLogicToSolve,
                            filename);

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

    public void init() {
        objectMapper = new ObjectMapper();
        selectNonogramsList();
    }

    public void selectNonogramsList() {
        if(sourceQuery.equals("logi")) {
            initParameters(1.0, 5.0, "logi");
            selectedNonogramsList = nonogramRepository.selectNonogramBySourceAndDifficulty(sources,
                    difficultyRange.get(0), difficultyRange.get(1));
        } else {
            initParameters(1.0, 5.0, "katana");
            selectedNonogramsList = nonogramRepository.selectNonogramsBySourceAndSize(sources,
                    30);
        }
    }

    public void initParameters(double minDifficulty, double maxDifficulty, String source) {
        difficultyRange = new ArrayList<>();
        difficultyRange.add(minDifficulty);
        difficultyRange.add(maxDifficulty);

        sources = new HashSet<>();
        sources.add(source);
    }

    public void extractValuesFromNonogram(Nonogram nonogram) {
        difficulty = nonogram.getDifficulty();
        filename = nonogram.getFilename();
        height = nonogram.getHeight();
        month = nonogram.getMonth();
        source = nonogram.getSource();
        width = nonogram.getWidth();
        year = nonogram.getYear();
    }
}