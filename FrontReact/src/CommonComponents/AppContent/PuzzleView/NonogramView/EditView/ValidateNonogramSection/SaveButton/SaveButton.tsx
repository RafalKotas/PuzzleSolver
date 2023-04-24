// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { AppState } from "../../../../../../../store"

// mui
import { Button } from "@mui/material"

interface OwnSaveButtonProps {
    disabled: boolean,
    toggleAlert: () => void
}

const mapStateToProps = (state: AppState) => ({
})
  
const mapDispatchToProps = (dispatch: Dispatch) => ({
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type SaveButtonPropsFromRedux = ConnectedProps<typeof connector>

type SaveButtonProps = SaveButtonPropsFromRedux & OwnSaveButtonProps

const SaveButton : React.FC<SaveButtonProps> = ({disabled, toggleAlert}) => {
    return (
        <Button 
            variant="contained"
            disabled={disabled}
            color={"primary"}
            style={{backgroundColor: disabled ? "#bec4c0" : "#e2ef6b", 
                color: "#4138eb", 
                marginLeft: 10, 
                marginRight: 10, 
                marginTop: 15,
                width: "80%"}}
            onClick={() => toggleAlert()}
        >
            SAVE
        </Button>
    )
}

export default connector(SaveButton)