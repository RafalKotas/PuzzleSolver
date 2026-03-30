// react-router
import { 
    Route, 
    Routes 
} from "react-router-dom"

// (sub)component(s)
import AkariEditView from "CommonComponents/AppContent/PuzzleView/AkariView/EditView/AkariEditView"
import AkariSolverView from "CommonComponents/AppContent/PuzzleView/AkariView/SolverView/AkariSolverView"
import ArchitectEditView from "CommonComponents/AppContent/PuzzleView/ArchitectView/EditView/ArchitectEditView"
import ArchitectSolverView from "CommonComponents/AppContent/PuzzleView/ArchitectView/SolverView/ArchitectSolverView"
import HitoriEditView from "CommonComponents/AppContent/PuzzleView/HitoriView/EditView/HitoriEditView"
import HitoriSolverView from "CommonComponents/AppContent/PuzzleView/HitoriView/SolverView/HitoriSolverView"
import NonogramEditView from "CommonComponents/AppContent/PuzzleView/NonogramView/EditView/NonogramEditView"
import NonogramSolverView from "CommonComponents/AppContent/PuzzleView/NonogramView/SolverView/NonogramSolverView"
import SlitherlinkEditView from "CommonComponents/AppContent/PuzzleView/SlitherlinkView/EditView/SlitherlinkEditView"
import SlitherlinkSolverView from "CommonComponents/AppContent/PuzzleView/SlitherlinkView/SolverView/SlitherlinkSolverView"
import SudokuEditView from "CommonComponents/AppContent/PuzzleView/SudokuView/EditView/SudokuEditView"
import SudokuSolverView from "CommonComponents/AppContent/PuzzleView/SudokuView/SolverView/SudokuSolverView"

const ViewContent = () => {

    return (
        <main id="main-app-content">
            <div
                id="inner-container"
                style={{
                    display: "flex",
                    flexDirection: "row"
                }}
            >
                <Routes>
                    <Route path="nonogram-solver/:filename" element={<NonogramSolverView />} />
                    <Route path="nonogram-editor/:filename" element={<NonogramEditView />} />

                    <Route path="sudoku-solver" element={<SudokuSolverView />} />
                    <Route path="sudoku-editor" element={<SudokuEditView />} />

                    <Route path="slitherlink-solver" element={<SlitherlinkSolverView />} />
                    <Route path="slitherlink-editor" element={<SlitherlinkEditView />} />

                    <Route path="akari-solver" element={<AkariSolverView />} />
                    <Route path="akari-editor" element={<AkariEditView />} />

                    <Route path="hitori-solver" element={<HitoriSolverView />} />
                    <Route path="hitori-editor" element={<HitoriEditView />} />
                    
                    <Route path="architect-solver" element={<ArchitectSolverView/>} />
                    <Route path="architect-editor" element={<ArchitectEditView/>} />
                </Routes>
            </div>
        </main>
    )
}

export default ViewContent