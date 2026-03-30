// (sub)component(s)
import SudokuPanel from "PuzzleSpecific/SudokuContent/SudokuPanel/SudokuPanel"
import SudokuSelectSection from "PuzzleSpecific/SudokuContent/SudokuSelectSection/SudokuSelectSection"

const SudokuContent = () => {
    return (
        <main id="main-app-content">
            {<div 
                id="inner-container"
                style={{
                    display: "flex",
                    flexDirection: "row"
                }}
            >
                <SudokuPanel/>
                <SudokuSelectSection/>
            </div>}
        </main>
    )
}

export default SudokuContent