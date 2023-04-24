// react
import { useEffect } from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../store"


// sub(components)
import HitoriDisplay from "../HitoriDisplay/HitoriDisplay"

const mapStateToProps = (state: AppState) => ({

})

const mapDispatchToProps = (dispatch : Dispatch) => ({

})

const connector = connect(mapStateToProps, mapDispatchToProps)

type HitoriEditViewPropsFromRedux = ConnectedProps<typeof connector>

type HitoriEditViewProps = HitoriEditViewPropsFromRedux

const HitoriEditView : React.FC<HitoriEditViewProps> = () => {

    useEffect(() => {
        //eslint-disable-next-line
    }, [])

    return (
        <div id="selected-hitori-view">
            {/*<ValidateHitoriSection />*/}
            <div
                id="edit-view-container"  
                style={{
                        overflowX: "auto",
                        overflowY: "auto",
                        flexDirection: "row"
                        }}
            >
                <HitoriDisplay/>
            </div>
            {/*<EditHitoriActions key={"edit-nonogram-actions"}/>*/}
        </div>
    )
}

export default connector(HitoriEditView)