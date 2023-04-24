// react
import { useEffect } from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../store"


// sub(components)
import AkariDisplay from "../AkariDisplay/AkariDisplay"

//styles
import "../AkariView.css"

const mapStateToProps = (state: AppState) => ({

})

const mapDispatchToProps = (dispatch : Dispatch) => ({

})

const connector = connect(mapStateToProps, mapDispatchToProps)

type AkariEditViewPropsFromRedux = ConnectedProps<typeof connector>

type AkariEditViewProps = AkariEditViewPropsFromRedux

const AkariEditView : React.FC<AkariEditViewProps> = () => {

    useEffect(() => {
        //eslint-disable-next-line
    }, [])

    return (
        <div id="selected-akari-view">
            {/*<ValidateAkariSection />*/}
            <div
                id="akari-view-container"  
                style={{
                    overflowX: "auto",
                    overflowY: "auto",
                    flexDirection: "row"
                }}
            >
                <AkariDisplay/>
            </div>
            {/*<EditAkariActions key={"edit-nonogram-actions"}/>*/}
        </div>
    )
}

export default connector(AkariEditView)