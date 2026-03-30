// (sub)component(s)
import AkariPanel from "PuzzleSpecific/AkariContent/AkariPanel/AkariPanel"
import AkariSelectSection from "PuzzleSpecific/AkariContent/AkariSelectSection/AkariSelectSection"

const AkariContent = () => {
    return (
        <main id="main-app-content">
            {<div 
                id="inner-container"
                style={{
                    display: "flex",
                    flexDirection: "row"
                }}
            >
                <AkariPanel/>
                <AkariSelectSection/>
            </div>}
        </main>
    )
}

export default AkariContent