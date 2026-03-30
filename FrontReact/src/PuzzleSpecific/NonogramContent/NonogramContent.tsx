// (sub)component(s)
import NonogramPanel from "PuzzleSpecific/NonogramContent/NonogramPanel/NonogramPanel"
import NonogramSelectSection from "PuzzleSpecific/NonogramContent/NonogramSelectSection/NonogramSelectSection"

const NonogramContent = () => {
    return (
        <main id="main-app-content">
            {<div 
                id="inner-container"
                style={{
                    display: "flex",
                    flexDirection: "row"
                }}
            >
                <NonogramPanel/>
                <NonogramSelectSection/>
            </div>}
        </main>
    )
}

export default NonogramContent