// react
import { useEffect } from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../store"


// sub(components)
import SlitherlinkDisplay from "../SlitherlinkDisplay/SlitherlinkDisplay"

const mapStateToProps = (state: AppState) => ({

})

const mapDispatchToProps = (dispatch : Dispatch) => ({

})

const connector = connect(mapStateToProps, mapDispatchToProps)

type SlitherlinkEditViewPropsFromRedux = ConnectedProps<typeof connector>

type SlitherlinkEditViewProps = SlitherlinkEditViewPropsFromRedux

const SlitherlinkEditView : React.FC<SlitherlinkEditViewProps> = () => {

    useEffect(() => {
        //eslint-disable-next-line
    }, [])

    return (
        <div id="selected-nonogram-view">
            {/*<ValidateSlitherlinkSection />*/}
            <div
                id="edit-view-container"  
                style={{
                        overflowX: "auto",
                        overflowY: "auto",
                        flexDirection: "row"
                        }}
            >
                <SlitherlinkDisplay/>
            </div>
            {/*<EditSlitherlinkActions key={"edit-nonogram-actions"}/>*/}
        </div>
    )
}

export default connector(SlitherlinkEditView)