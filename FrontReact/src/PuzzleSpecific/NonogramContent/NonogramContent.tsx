// (sub) components
import NonogramPanel from "./NonogramPanel/NonogramPanel"
import NonogramSelectSection from "./NonogramSelectSection/NonogramSelectSection"

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