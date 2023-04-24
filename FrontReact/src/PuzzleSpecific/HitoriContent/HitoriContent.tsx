// (sub) components
import HitoriPanel from "./HitoriPanel/HitoriPanel"
import HitoriSelectSection from "./HitoriSelectSection/HitoriSelectSection"

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