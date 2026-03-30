// (sub)component(s)
import SlitherlinkPanel from "PuzzleSpecific/SlitherlinkContent/SlitherlinkPanel/SlitherlinkPanel"
import SlitherlinkSelectSection from "PuzzleSpecific/SlitherlinkContent/SlitherlinkSelectSection/SlitherlinkSelectSection"

const SlitherlinkContent = () => {
    return (
        <main id="main-app-content">
            {<div 
                id="inner-container"
                style={{
                    display: "flex",
                    flexDirection: "row"
                }}
            >
                <SlitherlinkPanel/>
                <SlitherlinkSelectSection/>
            </div>}
        </main>
    )
}

export default SlitherlinkContent