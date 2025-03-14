// react
import React, { useEffect} from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../store"
import { InitOptionsList, nonogramFiltersObject, UpdateNumberValuedFilters, UpdateStringValuedFilters } from "../../../../../store/filters/nonogram"

//(sub) components
import Box from "@mui/material/Box"
import CheckBoxGroup from "../../../../../CommonComponents/AppContent/PuzzleFiltersPanel/CheckBoxGroup/CheckBoxGroup"
import CustomMUISlider from "../../../../../CommonComponents/AppContent/PuzzleFiltersPanel/CustomMUISlider/CustomMUISlider"

//functions
import commonFunctions from "../../../../../functions"

//styles
import "./NonogramFiltersSection.css"
import NonogramService from "../../../../../services/nonogram/nonogram.service"

interface OwnNonogramFiltersSectionProps {
    markFiltersCollected: () => void,
    showFilters: boolean
}

const mapStateToProps = (state: AppState) => ({

    nonogramsList: state.nonogramDataReducer.nonogramsList,

    allSources: state.nonogramFiltersReducer.sources,
    allYears: state.nonogramFiltersReducer.years,
    allMonths: state.nonogramFiltersReducer.months,
    allDifficulties: state.nonogramFiltersReducer.difficulties,
    allWidths: state.nonogramFiltersReducer.widths,
    allHeights: state.nonogramFiltersReducer.heights,

    selectedSources: state.nonogramFiltersReducer.selectedSources,
    selectedYears: state.nonogramFiltersReducer.selectedYears,
    selectedMonths: state.nonogramFiltersReducer.selectedMonths,
    selectedDifficulties: state.nonogramFiltersReducer.selectedDifficulties,
    selectedWidths: state.nonogramFiltersReducer.selectedWidths,
    selectedHeights: state.nonogramFiltersReducer.selectedHeights
})
  
const mapDispatchToProps = (dispatch: Dispatch) => ({
    
    initOptions: (options: nonogramFiltersObject) => 
        dispatch(InitOptionsList(options)),

    updateStringFilters: (optionName: string, filters: Array<string>) => {
        dispatch(UpdateStringValuedFilters(optionName, filters))
    },

    updateNumberFilters: (optionName: string, filters: Array<number>) => {
        dispatch(UpdateNumberValuedFilters(optionName, filters))
    }
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type NonogramFiltersSectionPropsFromRedux = ConnectedProps<typeof connector>

type NonogramFiltersSectionProps = NonogramFiltersSectionPropsFromRedux & OwnNonogramFiltersSectionProps

const NonogramFiltersSection : React.FC<NonogramFiltersSectionProps> = ({nonogramsList, showFilters, 
    initOptions, markFiltersCollected, updateStringFilters, updateNumberFilters,
    allSources, allYears, allMonths,
    allDifficulties, allHeights, allWidths,
    selectedSources, selectedYears, selectedMonths}) => {

    useEffect(() => {

       NonogramService.getNonogramFilters().then((response) => {
            let filtersObject : nonogramFiltersObject = response.data

            initOptions(filtersObject)

            markFiltersCollected()
        })

    
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

    // update widths
    const updateWidths = (widths: Array<number>) => {
        updateNumberFilters("selectedWidths", widths)
    }

    // update heights
    const updateHeights = (heights: Array<number>) => {
        updateNumberFilters("selectedHeights", heights)
    }

    const sliders = {
        "Difficulties :" : {
            step: 0.1,
            valuesArr: allDifficulties,
            //selectedValuesArr: selectedDifficulties,
            handler: updateDifficulties
        },
        "Widths :" : {
            step: 5,
            valuesArr: allWidths,
            //selectedValuesArr: selectedWidths,
            handler: updateWidths
        },
        "Heights :" : {
            step: 5,
            valuesArr: allHeights,
            //selectedHeightsArr: selectedHeights,
            handler: updateHeights
        }
    }

    return (
        <div id="nonogram-filter-section">
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
                                    key={"nonogram-filter-checkbox-" + label}
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
                                    key={label}
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

export default connect(mapStateToProps, mapDispatchToProps)(NonogramFiltersSection)