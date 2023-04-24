// react
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../store"
import { AddSlitherlinkDetail, slitherlinkInformation, ChangeSlitherlinkDetail} from "../../../../store/data/slitherlink"
import { modes, SetMode } from "../../../../store/display"

// (sub)component(s)
import InputSlider from "../../../../CommonComponents/AppContent/PuzzleFiltersPanel/InputSlider/InputSlider"

//other
import { slitherlinkSliders } from "./sliders"

interface OwnSlitherlinkSlidersProps {

}

const mapStateToProps = (state: AppState) => ({
    selectedSlitherlink: state.slitherlinkDataReducer.selectedSlitherlink,
    detailsSet: state.slitherlinkDataReducer.detailsSet
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    addSlitherlinkDetail: (property : keyof slitherlinkInformation) => dispatch(AddSlitherlinkDetail(property)),
    changeSlitherlinkDetail: (property : keyof slitherlinkInformation, value: string | number | number[]) => 
        dispatch(ChangeSlitherlinkDetail(property, value)),

    setMode: (updatedMode : modes) => dispatch(SetMode(updatedMode))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type SlitherlinkSlidersPropsFromRedux = ConnectedProps<typeof connector>

type SlitherlinkSlidersProps = SlitherlinkSlidersPropsFromRedux & OwnSlitherlinkSlidersProps

const SlitherlinkSliders : React.FC<SlitherlinkSlidersProps> = ({addSlitherlinkDetail, changeSlitherlinkDetail, detailsSet}) => {

    const handleNumberProperty = (property: string, value: number | Array<number>) => {
        if(!detailsSet.includes(property)) {
            addSlitherlinkDetail(property as keyof slitherlinkInformation)
        }
        changeSlitherlinkDetail(property as keyof slitherlinkInformation, value)
    }

    return (
        <React.Fragment>
            {
                slitherlinkSliders.map((slider) => {
                    return (
                        <InputSlider
                            label={slider.label}
                            propertyName={slider.propertyName}
                            minValue={slider.minValue}
                            initialValue={slider.initialValue}
                            maxValue={slider.maxValue}
                            step={slider.step}
                            passValueToParent={handleNumberProperty}
                        />
                    )
                })
            }
        </React.Fragment>
    )
}

export default connector(SlitherlinkSliders)