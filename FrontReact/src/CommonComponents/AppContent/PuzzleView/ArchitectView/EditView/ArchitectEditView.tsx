// react
import { useEffect } from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../store"


// sub(components)
import ArchitectDisplay from "../ArchitectDisplay/ArchitectDisplay"

const mapStateToProps = (state: AppState) => ({

})

const mapDispatchToProps = (dispatch : Dispatch) => ({

})

const connector = connect(mapStateToProps, mapDispatchToProps)

type ArchitectEditViewPropsFromRedux = ConnectedProps<typeof connector>

type ArchitectEditViewProps = ArchitectEditViewPropsFromRedux

const ArchitectEditView : React.FC<ArchitectEditViewProps> = () => {

    useEffect(() => {
        //eslint-disable-next-line
    }, [])

    return (
        <div id="selected-architect-view">
            {/*<ValidateArchitectSection />*/}
            <div
                id="edit-view-container"  
                style={{
                        overflowX: "auto",
                        overflowY: "auto",
                        flexDirection: "row"
                        }}
            >
                <ArchitectDisplay/>
            </div>
            {/*<EditArchitectActions key={"edit-nonogram-actions"}/>*/}
        </div>
    )
}

export default connector(ArchitectEditView)