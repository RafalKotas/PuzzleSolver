// mui
import { Tooltip } from "@mui/material"

// fontawesome
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import { faArrowUp, faArrowDown} from "@fortawesome/free-solid-svg-icons"

// redux
import { filterTypeDetails,  filterWithDirectionIncludedSelector, selectFilterAction } from "../../../../../store/filters/akari"
import { AppState } from "../../../../../store"
import { Dispatch } from "redux"
import { connect, ConnectedProps, useDispatch} from "react-redux"


interface OwnAkariSortFilterProps {
    filterDetails : filterTypeDetails,
    priorityIndicator: number
}

const mapStateToProps = (state: AppState, ownProps : OwnAkariSortFilterProps) => ({

    mode: state.displayReducer.mode,

    ascIncluded: filterWithDirectionIncludedSelector(state.akariFiltersReducer, 
        ownProps.filterDetails.filterName, "ascending"),

    descIncluded: filterWithDirectionIncludedSelector(state.akariFiltersReducer, 
            ownProps.filterDetails.filterName, "descending"),

    arrowUpAction: selectFilterAction(state.akariFiltersReducer, 
        {filterName: ownProps.filterDetails.filterName, sortDirection: "ascending"}),
    
    arrowDownAction: selectFilterAction(state.akariFiltersReducer, 
        {filterName: ownProps.filterDetails.filterName, sortDirection: "descending"}),
})

const mapDispatchToProps = (dispatch: Dispatch) => ({

})

const connector = connect(mapStateToProps, mapDispatchToProps)

type AkariSortFilterPropsFromRedux = ConnectedProps<typeof connector>

type AkariSortFilterProps = AkariSortFilterPropsFromRedux & OwnAkariSortFilterProps

const AkariSortFilter : React.FC<AkariSortFilterProps> = ({filterDetails, arrowUpAction, arrowDownAction,
ascIncluded, descIncluded, priorityIndicator}) => {

    const dispatch = useDispatch()

    const priorityIndicatorVisible = () => priorityIndicator === 100 ? "sort-filter-priority-masked" : "sort-filter-priority-visible"

    return (
        <div className="nonogram-sort-filter">
            <div className="sort-filter-selector">
                <Tooltip title={filterDetails.helperText.toLocaleUpperCase()} placement="top">
                    <FontAwesomeIcon 
                        icon={filterDetails.fontAwesomeIcon}
                        className="sort-filter-icon"
                        style={{height: "40px", width: "40px"}}
                    />
                </Tooltip>
                <div className="sort-filter-directions">
                    <Tooltip title={filterDetails.helperText + " sort ascending"} placement="bottom">
                        <FontAwesomeIcon
                            onClick={() => dispatch(arrowUpAction)}
                            style={{
                                backgroundColor: ascIncluded ? "#dee888" : ""
                            }}
                            className="sort-filter-arrow"
                            icon={faArrowUp}
                        />
                    </Tooltip>
                    <Tooltip title={filterDetails.helperText + " sort descending"} placement="bottom">
                        <FontAwesomeIcon
                            onClick={() => dispatch(arrowDownAction)}
                            style={{
                                backgroundColor: descIncluded ? "#dee888" : ""
                            }}
                            className="sort-filter-arrow"
                            icon={faArrowDown}
                        />
                    </Tooltip>
                </div>
            </div>
            <div className={"sort-filter-priority-indicator " +  priorityIndicatorVisible()}>
                {priorityIndicator === 100 ? "X" : priorityIndicator.toString()}
            </div>
        </div>
    )
}

export default connector(AkariSortFilter)