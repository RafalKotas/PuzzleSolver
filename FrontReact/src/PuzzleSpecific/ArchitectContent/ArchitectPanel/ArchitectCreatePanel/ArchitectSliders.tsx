// react
import React from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../store"
import { AddArchitectDetail, architectInformation, ChangeArchitectDetail} from "../../../../store/data/architect"
import { modes, SetMode } from "../../../../store/display"

// (sub)component(s)
import InputSlider from "../../../../CommonComponents/AppContent/PuzzleFiltersPanel/InputSlider/InputSlider"

//other
import { architectSliders } from "./sliders"

interface OwnArchitectSlidersProps {

}

const mapStateToProps = (state: AppState) => ({
    selectedArchitect: state.architectDataReducer.selectedArchitect,
    detailsSet: state.architectDataReducer.detailsSet
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    addArchitectDetail: (property : keyof architectInformation) => dispatch(AddArchitectDetail(property)),
    changeArchitectDetail: (property : keyof architectInformation, value: string | number | number[]) => 
        dispatch(ChangeArchitectDetail(property, value)),

    setMode: (updatedMode : modes) => dispatch(SetMode(updatedMode))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type ArchitectSlidersPropsFromRedux = ConnectedProps<typeof connector>

type ArchitectSlidersProps = ArchitectSlidersPropsFromRedux & OwnArchitectSlidersProps

const ArchitectSliders : React.FC<ArchitectSlidersProps> = ({addArchitectDetail, changeArchitectDetail, detailsSet}) => {

    const handleNumberProperty = (property: string, value: number | Array<number>) => {
        if(!detailsSet.includes(property)) {
            addArchitectDetail(property as keyof architectInformation)
        }
        changeArchitectDetail(property as keyof architectInformation, value)
    }

    return (
        <React.Fragment>
            {
                architectSliders.map((slider) => {
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

export default connector(ArchitectSliders)