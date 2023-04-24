// (sub) components
import ArchitectPanel from "./ArchitectPanel/ArchitectPanel"
import ArchitectSelectSection from "./ArchitectSelectSection/ArchitectSelectSection"

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