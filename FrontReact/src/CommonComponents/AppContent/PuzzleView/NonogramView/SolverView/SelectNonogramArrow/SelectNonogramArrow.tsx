// react - router
import { useNavigate } from "react-router-dom"

// redux
import { 
    connect, 
    ConnectedProps 
} from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "@store/index"
import { 
    findNextNonogramName, 
    findPreviousNonogramName, 
    selectedNonogramDetails, 
    SetSelectedNonogram 
} from "store/data/nonogram"

// fortawesome
import { IconDefinition } from "@fortawesome/free-solid-svg-icons"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"

interface OwnSelectNonogramArrowProps {
    arrowIcon: IconDefinition,
    fileName: string | null
}

const mapStateToProps = (state: AppState) => ({
    selectedNonogram: state.nonogramDataReducer.selectedNonogram,
    previousNonogramFilename: findPreviousNonogramName(state.displayReducer, state.nonogramDataReducer, state.nonogramFiltersReducer),
    nextNonogramFilename: findNextNonogramName(state.displayReducer, state.nonogramDataReducer, state.nonogramFiltersReducer)
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    setSelectedNonogram: (nonogram: selectedNonogramDetails | null) => dispatch(SetSelectedNonogram(nonogram))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type SelectNonogramArrowPropsFromRedux = ConnectedProps<typeof connector>

type SelectNonogramArrowProps = SelectNonogramArrowPropsFromRedux & OwnSelectNonogramArrowProps

const SelectNonogramArrow : React.FC<SelectNonogramArrowProps> = ({arrowIcon, fileName}) => {

    const navigate = useNavigate()

    return (
        <div className={"change-puzzle-arrow-wrapper"}>
            <FontAwesomeIcon 
                className={"change-puzzle-arrow"}
                icon={arrowIcon} 
                onClick={() => {
                    if (fileName) {
                        navigate("../nonogram-solver/" + fileName)
                    }
                }}
            />
        </div>
    )
}

export default connector(SelectNonogramArrow)