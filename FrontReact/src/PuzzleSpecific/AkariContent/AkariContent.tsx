// (sub) components
import AkariPanel from "./AkariPanel/AkariPanel"
import AkariSelectSection from "./AkariSelectSection/AkariSelectSection"

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