// react
import React, { useEffect } from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../store"

// (sub)component(s)
import InputSlider from "../../../../CommonComponents/AppContent/PuzzleFiltersPanel/InputSlider/InputSlider"

//mui
import { Button, ButtonProps, styled } from "@mui/material"
import { grey } from "@mui/material/colors"

// fontawesome
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import { faPlus } from "@fortawesome/free-solid-svg-icons"

// types, interfaces etc
import { SudokuStringProps } from "./types"

//other
import { sliders } from "./sliders"

// sub(components)
import StringPropTextField from "../../../../CommonComponents/StringPropTextField/StringPropTextField"
import { AddSudokuDetail, ChangeSudokuDetail, RemoveSudokuDetail, SaveTemporarySudoku, sudokuInformation } from "../../../../store/data/sudoku"
import { modes, SetMode } from "../../../../store/display"

interface OwnSudokuCreatePanelProps {

}

const mapStateToProps = (state: AppState) => ({
    selectedSudoku: state.sudokuDataReducer.selectedSudoku,
    sudokuCorrect: state.sudokuDataReducer.sudokuCorrect,
    detailsSet: state.sudokuDataReducer.detailsSet
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    addSudokuDetail: (property : keyof sudokuInformation) => dispatch(AddSudokuDetail(property)),
    removeSudokuDetail: (property : keyof sudokuInformation) => dispatch(RemoveSudokuDetail(property)),
    changeSudokuDetail: (property : keyof sudokuInformation, value: string | number | number[]) => 
        dispatch(ChangeSudokuDetail(property, value)),
    saveCreatedTemplate: () => dispatch(SaveTemporarySudoku()),

    setMode: (updatedMode : modes) => dispatch(SetMode(updatedMode))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type SudokuCreatePanelPropsFromRedux = ConnectedProps<typeof connector>

type SudokuCreatePanelProps = SudokuCreatePanelPropsFromRedux & OwnSudokuCreatePanelProps

const SudokuCreatePanel : React.FC<SudokuCreatePanelProps> = ({addSudokuDetail, removeSudokuDetail, 
    changeSudokuDetail, saveCreatedTemplate, detailsSet, setMode}) => {

    useEffect(() => {
        setMode("CREATE")
        // eslint-disable-next-line
    }, [])

    const handleNumberProperty = (property: string, value: number | Array<number>) => {
            if(!detailsSet.includes(property)) {
                addSudokuDetail(property as keyof sudokuInformation)
            }
            changeSudokuDetail(property as keyof sudokuInformation, value)
    }

    const handleStringProperty = (property: string, value: string, valid: boolean) => {
        if(!valid) {
            if(detailsSet.includes(property)) {
                removeSudokuDetail(property as keyof sudokuInformation)
            }
        } else if(valid) {
            if(!detailsSet.includes(property)) {
                addSudokuDetail(property as keyof sudokuInformation)
            }
            changeSudokuDetail(property as keyof sudokuInformation, value)
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
                            passValueToParent={handleNumberProperty}
                        />
                    )
                })
            }
            {
                SudokuStringProps.map((prop) => {
                    return <StringPropTextField 
                                prop={prop}
                                passValueToParent={handleStringProperty}
                            />
                })
            }
            <ColorButton onClick={() => saveCreatedTemplate()}
                disabled={detailsSet.length < 2} color="success">
                ADD<FontAwesomeIcon icon={faPlus}/>
            </ColorButton>
        </section>
    )
}

export default connector(SudokuCreatePanel)