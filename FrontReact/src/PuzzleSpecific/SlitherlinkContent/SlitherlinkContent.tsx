// (sub) components
import SlitherlinkPanel from "./SlitherlinkPanel/SlitherlinkPanel"
import SlitherlinkSelectSection from "./SlitherlinkSelectSection/SlitherlinkSelectSection"

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