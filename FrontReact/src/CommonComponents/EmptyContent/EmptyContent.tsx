// styles
import "./EmptyContent.css"

const EmptyContent= () => {

    return (
        <main id="main-app-content">
            {<div 
                id="inner-container"
                style={{
                    display: "flex",
                    flexDirection: "row"
                }}
            >
                EMPTY
            </div>}
        </main>
    )
}

export default EmptyContent