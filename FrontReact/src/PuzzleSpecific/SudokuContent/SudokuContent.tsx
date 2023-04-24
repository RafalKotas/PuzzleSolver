// (sub) components
import SudokuPanel from "./SudokuPanel/SudokuPanel"
import SudokuSelectSection from "./SudokuSelectSection/SudokuSelectSection"

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