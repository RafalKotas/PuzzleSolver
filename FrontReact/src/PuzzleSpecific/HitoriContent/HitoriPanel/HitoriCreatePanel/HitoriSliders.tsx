// react
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../store"
import { AddHitoriDetail, hitoriInformation, ChangeHitoriDetail} from "../../../../store/data/hitori"
import { modes, SetMode } from "../../../../store/display"

// (sub)component(s)
import InputSlider from "../../../../CommonComponents/AppContent/PuzzleFiltersPanel/InputSlider/InputSlider"

//other
import { hitoriSliders } from "./sliders"

interface OwnHitoriSlidersProps {

}

const mapStateToProps = (state: AppState) => ({
    selectedHitori: state.hitoriDataReducer.selectedHitori,
    HitoriCorrect: state.hitoriDataReducer.hitoriCorrect,
    detailsSet: state.hitoriDataReducer.detailsSet
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    addHitoriDetail: (property : keyof hitoriInformation) => dispatch(AddHitoriDetail(property)),
    changeHitoriDetail: (property : keyof hitoriInformation, value: string | number | number[]) => 
        dispatch(ChangeHitoriDetail(property, value)),

    setMode: (updatedMode : modes) => dispatch(SetMode(updatedMode))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type HitoriSlidersPropsFromRedux = ConnectedProps<typeof connector>

type HitoriSlidersProps = HitoriSlidersPropsFromRedux & OwnHitoriSlidersProps

const HitoriSliders : React.FC<HitoriSlidersProps> = ({addHitoriDetail, changeHitoriDetail, detailsSet}) => {

    const handleNumberProperty = (property: string, value: number | Array<number>) => {
        if(!detailsSet.includes(property)) {
            addHitoriDetail(property as keyof hitoriInformation)
        }
        changeHitoriDetail(property as keyof hitoriInformation, value)
    }

    return (
        <React.Fragment>
            {
                hitoriSliders.map((slider) => {
                    return (
                        <InputSlider
                            label={slider.label}
                            propertyName={slider.propertyName}
                            minValue={slider.minValue}
                            initialValue={slider.initialValue}
                            maxValue={slider.maxValue}
                            step={slider.step}
                            icon={slider.icon}
                            passValueToParent={handleNumberProperty}
                        />
                    )
                })
            }
        </React.Fragment>
    )
}

export default connector(HitoriSliders)