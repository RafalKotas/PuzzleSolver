// react

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../../../store"
import { ClosePreviewNonogram, PreviewNonogram, SetEditMode} from "../../../../../../../store/data/nonogram"

// mui
import { Button} from "@mui/material"


interface OwnPreviewButtonProps {
    passModeToParent: (editMode: boolean) => void
}

const mapStateToProps = (state: AppState) => ({
    editMode: state.nonogramDataReducer.editMode
})
  
const mapDispatchToProps = (dispatch: Dispatch) => ({
    togglePreviewMode: () => dispatch(PreviewNonogram()),
    toggleEditMode: () => dispatch(ClosePreviewNonogram()),
    setEditMode: (editMode : boolean) => dispatch(SetEditMode(editMode)),
    previewNonogram: () => dispatch(PreviewNonogram()),
    closeNonogramPreview: () => dispatch(ClosePreviewNonogram())
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type PreviewButtonPropsFromRedux = ConnectedProps<typeof connector>

type PreviewButtonProps = PreviewButtonPropsFromRedux & OwnPreviewButtonProps

const PreviewButton : React.FC<PreviewButtonProps> = ({editMode, previewNonogram, closeNonogramPreview}) => {

    const onPreviewClick = (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        previewNonogram()
    }

    const onEditClick = (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        closeNonogramPreview()
    }
    
    return (
        <Button 
            variant="contained"
            color={"primary"}
            style={{marginLeft: 10, marginRight: 10, marginTop: 15, width: "80%"}}
            onClick={(event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => editMode ? onPreviewClick(event) : onEditClick(event)}
        >
            {editMode ? "PREVIEW" : "EDIT"}
        </Button>
    )
}

export default connector(PreviewButton)