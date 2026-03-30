// (sub)component(s)
import ArchitectPanel from "PuzzleSpecific/ArchitectContent/ArchitectPanel/ArchitectPanel"
import ArchitectSelectSection from "PuzzleSpecific/ArchitectContent/ArchitectSelectSection/ArchitectSelectSection"

const ArchitectContent = () => {
    return (
        <main id="main-app-content">
            {<div 
                id="inner-container"
                style={{
                    display: "flex",
                    flexDirection: "row"
                }}
            >
                <ArchitectPanel/>
                <ArchitectSelectSection/>
            </div>}
        </main>
    )
}

export default ArchitectContent