import React from "react"
import ReactDOM from "react-dom/client"
import "./index.css"
import App from "./App"
import reportWebVitals from "./reportWebVitals"
import {
  BrowserRouter,
  Routes,
  Route
} from "react-router-dom"
import EmptyContent from "./CommonComponents/EmptyContent/EmptyContent"

// content components
import NonogramContent from "./PuzzleSpecific/NonogramContent/NonogramContent"
import AkariContent from "./PuzzleSpecific/AkariContent/AkariContent"
import SudokuContent from "./PuzzleSpecific/SudokuContent/SudokuContent"
import SlitherlinkContent from "./PuzzleSpecific/SlitherlinkContent/SlitherlinkContent"
import HitoriContent from "./PuzzleSpecific/HitoriContent/HitoriContent"
import ArchitectContent from "./PuzzleSpecific/ArchitectContent/ArchitectContent"

// view components
import ViewContent from "./CommonComponents/ViewContent/ViewContent"

//solver-views
import NonogramSolverView from "./CommonComponents/AppContent/PuzzleView/NonogramView/SolverView/NonogramSolverView"
import SudokuSolverView from "./CommonComponents/AppContent/PuzzleView/SudokuView/SolverView/SudokuSolverView"
import AkariSolverView from "./CommonComponents/AppContent/PuzzleView/AkariView/SolverView/AkariSolverView"
import HitoriSolverView from "./CommonComponents/AppContent/PuzzleView/HitoriView/SolverView/HitoriSolverView"
import ArchitectSolverView from "./CommonComponents/AppContent/PuzzleView/ArchitectView/SolverView/ArchitectSolverView"
import SlitherlinkSolverView from "./CommonComponents/AppContent/PuzzleView/SlitherlinkView/SolverView/SlitherlinkSolverView"

// edit-views
import NonogramEditView from "./CommonComponents/AppContent/PuzzleView/NonogramView/EditView/NonogramEditView"
import SlitherlinkEditView from "./CommonComponents/AppContent/PuzzleView/SlitherlinkView/EditView/SlitherlinkEditView"
import AkariEditView from "./CommonComponents/AppContent/PuzzleView/AkariView/EditView/AkariEditView"
import SudokuEditView from "./CommonComponents/AppContent/PuzzleView/SudokuView/EditView/SudokuEditView"
import HitoriEditView from "./CommonComponents/AppContent/PuzzleView/HitoriView/EditView/HitoriEditView"
import ArchitectEditView from "./CommonComponents/AppContent/PuzzleView/ArchitectView/EditView/ArchitectEditView"

const root = ReactDOM.createRoot(
  document.getElementById("root") as HTMLElement
);
root.render(
  <React.StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="*" element={<App />}>

          <Route path="" element={<EmptyContent/>} /> 

          <Route path="nonogram" element={<NonogramContent/>}></Route>
          <Route path="akari" element={<AkariContent/>}></Route>
          <Route path="sudoku" element={<SudokuContent/>}></Route>
          <Route path="slitherlink" element={<SlitherlinkContent/>}></Route>
          <Route path="hitori" element={<HitoriContent/>}></Route>
          <Route path="architect" element={<ArchitectContent/>}></Route>

          <Route path="view/*" element={<ViewContent/>}>
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
          </Route>

        </Route>
      </Routes>
    </BrowserRouter>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.loog))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
