// (sub)component(s)
import HitoriPanel from "PuzzleSpecific/HitoriContent/HitoriPanel/HitoriPanel"
import HitoriSelectSection from "PuzzleSpecific/HitoriContent/HitoriSelectSection/HitoriSelectSection"

const HitoriContent = () => {
    return (
        <main id="main-app-content">
            {<div 
                id="inner-container"
                style={{
                    display: "flex",
                    flexDirection: "row"
                }}
            >
                <HitoriPanel/>
                <HitoriSelectSection/>
            </div>}
        </main>
    )
}

export default HitoriContent