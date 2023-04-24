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
import { akariOptions, InitOptionsList, UpdateNumberValuedFilters, UpdateStringValuedFilters } from "../../../../../store/filters/akari"
import { possibleAkariOptions } from "../../../../../store/data/akari"

interface OwnAkariFiltersSectionProps {
    markFiltersCollected: () => void,
    showFilters: boolean
}

const mapStateToProps = (state: AppState) => ({

    akarisList: state.akariDataReducer.akarisList,

    allSources: state.akariFiltersReducer.sources,
    allDifficulties: state.akariFiltersReducer.difficulties,
    allWidths: state.akariFiltersReducer.widths,
    allHeights: state.akariFiltersReducer.heights,

    selectedSources: state.akariFiltersReducer.selectedSources
})
  
const mapDispatchToProps = (dispatch: Dispatch) => ({
    
    initOptions: (options: akariOptions) => 
        dispatch(InitOptionsList(options)),

    updateStringFilters: (optionName: string, filters: Array<string>) => {
        dispatch(UpdateStringValuedFilters(optionName, filters))
    },

    updateNumberFilters: (optionName: string, filters: Array<number>) => {
        dispatch(UpdateNumberValuedFilters(optionName, filters))
    }
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type AkariFiltersSectionPropsFromRedux = ConnectedProps<typeof connector>

type AkariFiltersSectionProps = AkariFiltersSectionPropsFromRedux & OwnAkariFiltersSectionProps

const AkariFiltersSection : React.FC<AkariFiltersSectionProps> = ({akarisList, showFilters, 
    initOptions, markFiltersCollected, updateStringFilters, updateNumberFilters,
    allSources, allDifficulties, allHeights, allWidths,
    selectedSources}) => {

    useEffect(() => {

        let initialOptions : akariOptions = {
            "source": [] as string[],
            "difficulty": [] as number[],
            "width": [] as number[],
            "height": [] as number[],
        }
    
        for(let optionName of possibleAkariOptions) {
    
            let optionNameCasted = optionName as keyof akariOptions
            let optionValues = akarisList.map(akariInfo => {return akariInfo[optionNameCasted]})
    

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

    const checkBoxGroups = {
        "Sources" : {
            options: allSources,
            selectedOptions: selectedSources,
            onHandleChange: updateSources
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
        <div id="akari-filter-section">
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
                                    key={"akari-filter-checkBoxGroup-" + label}
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
                                    key={"akari-slider-" + label}
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

export default connect(mapStateToProps, mapDispatchToProps)(AkariFiltersSection)