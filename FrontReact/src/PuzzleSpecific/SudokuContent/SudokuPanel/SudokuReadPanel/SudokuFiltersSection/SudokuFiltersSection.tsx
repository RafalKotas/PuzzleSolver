// react
import React, { useEffect} from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../store"
import { sudokuOptions, InitOptionsList, UpdateNumberValuedFilters, UpdateStringValuedFilters } from "../../../../../store/filters/sudoku"
import { possibleSudokuOptions } from "../../../../../store/data/sudoku"

// (sub) components
import Box from "@mui/material/Box"
import CheckBoxGroup from "../../../../../CommonComponents/AppContent/PuzzleFiltersPanel/CheckBoxGroup/CheckBoxGroup"
import CustomMUISlider from "../../../../../CommonComponents/AppContent/PuzzleFiltersPanel/CustomMUISlider/CustomMUISlider"

// functions
import commonFunctions from "../../../../../functions"

interface OwnSudokuFiltersSectionProps {
    markFiltersCollected: () => void,
    showFilters: boolean
}

const mapStateToProps = (state: AppState) => ({

    sudokusList: state.sudokuDataReducer.sudokusList,

    allSources: state.sudokuFiltersReducer.sources,
    allYears: state.sudokuFiltersReducer.years,
    allMonths: state.sudokuFiltersReducer.months,

    selectedSources: state.sudokuFiltersReducer.selectedSources,
    selectedYears: state.sudokuFiltersReducer.selectedYears,
    selectedMonths: state.sudokuFiltersReducer.selectedMonths,

    allDifficulties: state.sudokuFiltersReducer.difficulties
})
  
const mapDispatchToProps = (dispatch: Dispatch) => ({
    
    initOptions: (options: sudokuOptions) => 
        dispatch(InitOptionsList(options)),

    updateStringFilters: (optionName: string, filters: Array<string>) => {
        dispatch(UpdateStringValuedFilters(optionName, filters))
    },

    updateNumberFilters: (optionName: string, filters: Array<number>) => {
        dispatch(UpdateNumberValuedFilters(optionName, filters))
    }
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type SudokuFiltersSectionPropsFromRedux = ConnectedProps<typeof connector>

type SudokuFiltersSectionProps = SudokuFiltersSectionPropsFromRedux & OwnSudokuFiltersSectionProps

const SudokuFiltersSection : React.FC<SudokuFiltersSectionProps> = ({ sudokusList, showFilters, 
    initOptions, markFiltersCollected, updateStringFilters, updateNumberFilters,
    allSources, allYears, allMonths, allDifficulties, selectedSources, selectedYears, selectedMonths }) => {

    useEffect(() => {

        let initialOptions : sudokuOptions = {
            "source": [] as string[],
            "year": [] as string[],
            "month": [] as string[],
            "difficulty": [] as number[],
            "filled": [] as number[]
        }
    
        for(let optionName of possibleSudokuOptions) {
    
            let optionNameCasted = optionName as keyof sudokuOptions
            let optionValues = sudokusList.map(sudokuInfo => {return sudokuInfo[optionNameCasted]})
    

            if(true) {
                //sorted unique options values
                let uniqueValues = commonFunctions.removeDuplicatesFromArray(optionValues).sort()
        
                initialOptions = {...initialOptions, [optionNameCasted]: uniqueValues}
            }
        }
    
        initOptions(initialOptions)

        markFiltersCollected()
    
        // eslint-disable-next-line
    }, [])
    
    // update sources
    const updateSources = (sources: Array<string>) => {
        updateStringFilters("selectedSources", sources)
    }

    // update years
    const updateYears = (years: Array<string>) => {
        updateStringFilters("selectedYears", years)
    }

    // update months
    const updateMonths = (months: Array<string>) => {
        updateStringFilters("selectedMonths", months)
    }

    const checkBoxGroups = {
        "Sources" : {
            options: allSources,
            selectedOptions: selectedSources,
            onHandleChange: updateSources
        },
        "Years" : {
            options: allYears,
            selectedOptions: selectedYears,
            onHandleChange: updateYears
        },
        "Months" : {
            options: allMonths,
            selectedOptions: selectedMonths,
            onHandleChange: updateMonths
        }
    }

    // update difficulties
    const updateDifficulties = (difficulties: Array<number>) => {
        updateNumberFilters("selectedDifficulties", difficulties)
    }

    const sliders = {
        "Difficulties :" : {
            step: 0.1,
            valuesArr: allDifficulties,
            handler: updateDifficulties
        }
    }

    return (
        <div id="sudoku-filter-section">
            <h1>FILTERS:</h1>
            {showFilters && 
                <React.Fragment>
                    <Box sx={{
                        display: "flex",
                        flexDirection: "column",
                        alignItems: "center",
                        justifyContent: "space-evenly",
                        backgroundColor: "white",
                        borderColor: "rgba(0, 0, 0, 0.1)",
                        borderWidth: "1px",
                        borderBottomRightRadius: "4px",
                        borderBottomLeftRadius: "4px"
                    }}>
                        {
                            Object.entries(checkBoxGroups).map(([label, value]) => {
                                return <CheckBoxGroup
                                    key={label}
                                    label={label}
                                    options={value.options}
                                    selectedOptions={value.selectedOptions}
                                    onHandleChange={value.onHandleChange}
                                />
                            })
                        }
                    </Box>
                    <Box sx={{
                        backgroundColor: "white",
                        borderColor: "rgba(0, 0, 0, 0.1)",
                        borderWidth: "1px",
                        display: "flex",
                        flexDirection: "column",
                        alignItems: "center",
                        justifyContent: "space-evenly",
                        paddingTop: "15px",
                        borderBottomRightRadius: "4px",
                        borderBottomLeftRadius: "4px"
                    }}>
                    {
                        Object.entries(sliders).map(([label, values]) => {
                            return (
                                <CustomMUISlider
                                    key={"sudoku-slider-" + label}
                                    minValue={commonFunctions.minValInArray(values.valuesArr)}
                                    maxValue={commonFunctions.maxValInArray(values.valuesArr)}
                                    step={values.step}
                                    filterLabel={label}
                                    onHandleChange={values.handler}
                                />
                            )
                        })
                    }
                    </Box>
                </React.Fragment>
            }
        </div>
    )
}

export default connect(mapStateToProps, mapDispatchToProps)(SudokuFiltersSection)