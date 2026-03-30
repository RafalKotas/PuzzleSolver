// react
import React, { useEffect } from "react"

// redux
import { 
    connect, 
    ConnectedProps 
} from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "@store/index"
import { 
    AddNonogramDetail, 
    RemoveNonogramDetail, 
    ChangeNonogramDetail, 
    SaveTemporaryNonogram, 
    nonogramInformation 
} from "store/data/nonogram"
import { 
    modes, 
    SetMode 
} from "store/display"
import { ResetNonogramBoard } from "store/puzzleLogic/nonogram"

// (sub)component(s)
import InputSlider from "CommonComponents/AppContent/PuzzleFiltersPanel/InputSlider/InputSlider"
import StringPropTextField from "CommonComponents/StringPropTextField/StringPropTextField"

// mui - components
import { 
    Button, 
    ButtonProps, 
    styled 
} from "@mui/material"
import { grey } from "@mui/material/colors"

// fortawesome
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import { faPlus } from "@fortawesome/free-solid-svg-icons"

// other
import { sliders } from "PuzzleSpecific/NonogramContent/NonogramPanel/NonogramCreatePanel/sliders"
import { nonogramStringProps } from "PuzzleSpecific/NonogramContent/NonogramPanel/NonogramCreatePanel/types"

// styles
import "./NonogramCreatePanel.css"

interface OwnNonogramCreatePanelProps {

}

const mapStateToProps = (state: AppState) => ({
    selectedNonogram: state.nonogramDataReducer.selectedNonogram,
    nonogramCorrect: state.nonogramDataReducer.nonogramCorrect,
    detailsSet: state.nonogramDataReducer.detailsSet
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    addNonogramDetail: (property : keyof nonogramInformation) => dispatch(AddNonogramDetail(property)),
    removeNonogramDetail: (property : keyof nonogramInformation) => dispatch(RemoveNonogramDetail(property)),
    changeNonogramDetail: (property : keyof nonogramInformation, value: string | number | number[] | object) => 
        dispatch(ChangeNonogramDetail(property, value)),
    saveCreatedTemplate: () => dispatch(SaveTemporaryNonogram()),
    resetNonogramBoard: () => dispatch(ResetNonogramBoard()),
    setMode: (updatedMode : modes) => dispatch(SetMode(updatedMode))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type NonogramCreatePanelPropsFromRedux = ConnectedProps<typeof connector>

type NonogramCreatePanelProps = NonogramCreatePanelPropsFromRedux & OwnNonogramCreatePanelProps

const NonogramCreatePanel : React.FC<NonogramCreatePanelProps> = ({detailsSet, addNonogramDetail, removeNonogramDetail, 
    changeNonogramDetail, saveCreatedTemplate, resetNonogramBoard, setMode}) => {

    useEffect(() => {
        setMode("CREATE")
        resetNonogramBoard()
        // eslint-disable-next-line
    }, [])

    const handleNumberProperty = (property: string, value: number | Array<number>) => {
            if (!detailsSet.includes(property)) {
                addNonogramDetail(property as keyof nonogramInformation)
            }
            changeNonogramDetail(property as keyof nonogramInformation, value)
    }

    const handleStringProperty = (property: string, value: string, valid: boolean) => {
        if (!valid) {
            if (detailsSet.includes(property)) {
                removeNonogramDetail(property as keyof nonogramInformation)
            }
        } else if (valid) {
            if (!detailsSet.includes(property)) {
                addNonogramDetail(property as keyof nonogramInformation)
            }
            changeNonogramDetail(property as keyof nonogramInformation, value)
        }
    }

    
    const ColorButton = styled(Button)<ButtonProps>(({ theme }) => ({
        color: theme.palette.getContrastText(grey[700]),
        backgroundColor: grey[500],
        "&:hover": {
          backgroundColor: grey[700],
        },
    }));

    return (
        <section className="puzzle-options-section">
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
                            icon={slider.icon}
                            passValueToParent={handleNumberProperty}
                        />
                    )
                })
            }
            {
                nonogramStringProps.map((prop) => {
                    return <StringPropTextField 
                                prop={prop}
                                passValueToParent={handleStringProperty}
                            />
                })
            }
            <ColorButton onClick={() => saveCreatedTemplate()}
                disabled={detailsSet.length < 6} color="success">
                ADD <FontAwesomeIcon icon={faPlus}/>
            </ColorButton>
        </section>
    )
}

export default connector(NonogramCreatePanel)