// react
import React, { useEffect} from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../store"

//(sub) components
import Box from "@mui/material/Box"
import CheckBoxGroup from "../../../../../CommonComponents/AppContent/PuzzleFiltersPanel/CheckBoxGroup/CheckBoxGroup"
import CustomMUISlider from "../../../../../CommonComponents/AppContent/PuzzleFiltersPanel/CustomMUISlider/CustomMUISlider"

//functions
import commonFunctions from "../../../../../functions"
import { slitherlinkOptions, InitOptionsList, UpdateNumberValuedFilters, UpdateStringValuedFilters } from "../../../../../store/filters/slitherlink"
import { possibleSlitherlinkOptions } from "../../../../../store/data/slitherlink"

interface OwnSlitherlinkFiltersSectionProps {
    markFiltersCollected: () => void,
    showFilters: boolean
}

const mapStateToProps = (state: AppState) => ({

    slitherlinksList: state.slitherlinkDataReducer.slitherlinksList,

    allSources: state.slitherlinkFiltersReducer.sources,
    allYears: state.slitherlinkFiltersReducer.years,
    allMonths: state.slitherlinkFiltersReducer.months,
    allDifficulties: state.slitherlinkFiltersReducer.difficulties,
    allWidths: state.slitherlinkFiltersReducer.widths,
    allHeights: state.slitherlinkFiltersReducer.heights,

    selectedSources: state.slitherlinkFiltersReducer.selectedSources,
    selectedYears: state.slitherlinkFiltersReducer.selectedYears,
    selectedMonths: state.slitherlinkFiltersReducer.selectedMonths
})
  
const mapDispatchToProps = (dispatch: Dispatch) => ({
    
    initOptions: (options: slitherlinkOptions) => 
        dispatch(InitOptionsList(options)),

    updateStringFilters: (optionName: string, filters: Array<string>) => {
        dispatch(UpdateStringValuedFilters(optionName, filters))
    },

    updateNumberFilters: (optionName: string, filters: Array<number>) => {
        dispatch(UpdateNumberValuedFilters(optionName, filters))
    }
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type SlitherlinkFiltersSectionPropsFromRedux = ConnectedProps<typeof connector>

type SlitherlinkFiltersSectionProps = SlitherlinkFiltersSectionPropsFromRedux & OwnSlitherlinkFiltersSectionProps

const SlitherlinkFiltersSection : React.FC<SlitherlinkFiltersSectionProps> = ({slitherlinksList, showFilters, 
    initOptions, markFiltersCollected, updateStringFilters, updateNumberFilters,
    allSources, allYears, allMonths, allDifficulties, allHeights, allWidths, 
    selectedSources, selectedYears, selectedMonths}) => {

    useEffect(() => {

        let initialOptions : slitherlinkOptions = {
            "source": [] as string[],
            "year": [] as string[],
            "month": [] as string[],
            "difficulty": [] as number[],
            "width": [] as number[],
            "height": [] as number[],
        }
    
        for(let optionName of possibleSlitherlinkOptions) {
    
            let optionNameCasted = optionName as keyof slitherlinkOptions
            let optionValues = slitherlinksList.map(slitherlinkInfo => {return slitherlinkInfo[optionNameCasted]})
    

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
            handler: updateDifficulties
        },
        "Widths :" : {
            step: 5,
            valuesArr: allWidths,
            handler: updateWidths
        },
        "Heights :" : {
            step: 5,
            valuesArr: allHeights,
            handler: updateHeights
        }
    }

    return (
        <div id="slitherlink-filter-section">
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
                                    key={"slitherlink-checkBoxGroup-" + label}
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
                                    key={"slitherlink-slider-" + label}
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

export default connect(mapStateToProps, mapDispatchToProps)(SlitherlinkFiltersSection)