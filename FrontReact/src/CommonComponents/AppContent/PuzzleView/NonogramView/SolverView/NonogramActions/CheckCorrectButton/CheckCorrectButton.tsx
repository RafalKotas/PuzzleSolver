// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../../../store"
import { correctnessIndicator } from "../../../../../../../store/data/nonogram/types"
import { SetCorrectness } from "../../../../../../../store/data/nonogram"

// mui
import { Button} from "@mui/material"

// functions
import commonFunctions from "../../../../../../../functions"

interface OwnCheckCorrectButtonProps {

}

const mapStateToProps = (state: AppState) => ({
    selectedNonogram: state.nonogramDataReducer.selectedNonogram,
    correctIndicator: state.nonogramDataReducer.nonogramCorrect,
    editMode: state.nonogramDataReducer.editMode
})
  
const mapDispatchToProps = (dispatch: Dispatch) => ({
    setCorrectness: (correct: correctnessIndicator) => 
    dispatch(SetCorrectness(correct)),
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type CheckCorrectButtonPropsFromRedux = ConnectedProps<typeof connector>

type CheckCorrectButtonProps = CheckCorrectButtonPropsFromRedux & OwnCheckCorrectButtonProps

const CheckCorrectButton : React.FC<CheckCorrectButtonProps> = ({selectedNonogram, correctIndicator, setCorrectness, editMode}) => {
    
    const correctnessButtonCheckLabel = () => {
        switch(correctIndicator) {
            case -1:
              return "NONOGRAM INCORRECT"
            case 1:
              return "NONOGRAM CORRECT"
            default:
              return "CHECK CORRECTNESS"
          }
    }

    const returnProperButtonVariant = () => {
        switch(correctIndicator) {
            case -1:
              return "error"
            case 0:
              return "warning"
            case 1:
              return "success"
            default:
              return "warning"
        }
    }

    return (
        <Button 
            variant="contained"
            color={returnProperButtonVariant()}
            style={{marginLeft: 10, marginRight: 10, marginTop: 15}}
            onClick={() => {
              if (selectedNonogram) {
                setCorrectness(commonFunctions.checkNonogramCorrectness(selectedNonogram))
              }
            }
            }
            disabled={editMode}
        >
            {correctnessButtonCheckLabel()}
        </Button>
    )
}

export default connector(CheckCorrectButton)