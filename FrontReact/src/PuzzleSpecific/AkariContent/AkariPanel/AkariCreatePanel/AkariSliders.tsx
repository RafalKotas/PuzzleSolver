// react
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../store"
import { AddAkariDetail, akariInformation, ChangeAkariDetail} from "../../../../store/data/akari"
import { modes, SetMode } from "../../../../store/display"

// (sub)component(s)
import InputSlider from "../../../../CommonComponents/AppContent/PuzzleFiltersPanel/InputSlider/InputSlider"

//other
import { sliders } from "./sliders"

interface OwnAkariSlidersProps {

}

const mapStateToProps = (state: AppState) => ({
    selectedAkari: state.akariDataReducer.selectedAkari,
    AkariCorrect: state.akariDataReducer.akariCorrect,
    detailsSet: state.akariDataReducer.detailsSet
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    addAkariDetail: (property : keyof akariInformation) => dispatch(AddAkariDetail(property)),
    changeAkariDetail: (property : keyof akariInformation, value: string | number | number[]) => 
        dispatch(ChangeAkariDetail(property, value)),

    setMode: (updatedMode : modes) => dispatch(SetMode(updatedMode))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type AkariSlidersPropsFromRedux = ConnectedProps<typeof connector>

type AkariSlidersProps = AkariSlidersPropsFromRedux & OwnAkariSlidersProps

const AkariSliders : React.FC<AkariSlidersProps> = ({addAkariDetail, changeAkariDetail, detailsSet}) => {

    const handleNumberProperty = (property: string, value: number | Array<number>) => {
        if(!detailsSet.includes(property)) {
            addAkariDetail(property as keyof akariInformation)
        }
        changeAkariDetail(property as keyof akariInformation, value)
    }

    return (
        <React.Fragment>
            {
                sliders.map((slider) => {
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

export default connector(AkariSliders)