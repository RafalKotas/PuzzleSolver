// react
import { useEffect, useState } from "react"

// redux
import { Dispatch } from "redux"
import { connect, ConnectedProps } from "react-redux"

// redux - store
import { AppState } from "../../../../../../../store"

// services
import NonogramService from "../../../../../../../services/nonogram/nonogram.service"

// mui
import {IconButton, Button, Dialog, DialogTitle, DialogActions, DialogContent, DialogContentText, TextField} from "@mui/material"
import CloseIcon from "@mui/icons-material/Close"

// fontawesome
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import { faRightLong } from "@fortawesome/free-solid-svg-icons"
import SudokuService from "../../../../../../../services/sudoku/sudoku.service"
import { calculateFilledFields } from "../../../../../../../store/puzzleLogic/sudoku/logic"

interface OwnSaveModalProps {
    showSaveAlert: boolean,
    passShowSaveAlert: (show: boolean) => void,
    passSavedFileName: (fileName: string) => void
}

const mapStateToProps = (state: AppState) => ({
    puzzleName: state.commonReducer.selectedPuzzleName,
    selectedNonogram: state.nonogramDataReducer.selectedNonogram,
    selectedSudoku: state.sudokuDataReducer.selectedSudoku,
    createdSudoku: state.sudokuDataReducer.createdSudoku,
    mode: state.displayReducer.mode
})

const mapDispatchToProps = (dispatch: Dispatch) => ({

})

const connector = connect(mapStateToProps, mapDispatchToProps)

type SaveModalPropsFromRedux = ConnectedProps<typeof connector>

type SaveModalProps = SaveModalPropsFromRedux & OwnSaveModalProps

const SaveModal : React.FC<SaveModalProps> = ({puzzleName, showSaveAlert, passShowSaveAlert, passSavedFileName, 
    selectedNonogram, mode, selectedSudoku, createdSudoku}) => {

    const [fileName, setFileName] = useState<string>("")
    const [responseText, setResponseText] = useState<string>("")

    useEffect(() => {
        setResponseText("")
    }, [showSaveAlert])

    useEffect(() => {
        if(checkSaveSuccess(responseText)) {
            passShowSaveAlert(false)
            passSavedFileName(fileName)
        }
        //eslint-disable-next-line
    }, [responseText])

    const inputChangeHandler = (event: React.ChangeEvent<HTMLInputElement>) => {
        if(responseText.length > 0) {
            setResponseText("")
        }
        setFileName( event.target.value )
    }

    const keyHandler = (event: React.KeyboardEvent<HTMLDivElement>) => {
        if(showSaveAlert && event.key === "Enter" && fileName.length >= 3) {
            sendPuzzleToSave()
        }
    }

    interface DialogTitleProps {
        id: string;
        children?: React.ReactNode;
        onClose: () => void;
    }

    const BootstrapDialogTitle = (props: DialogTitleProps) => {
        const { children, onClose, ...other } = props;
      
        return (
          <DialogTitle sx={{ m: 0, p: 2 }} {...other}>
            {children}
            {onClose ? (
              <IconButton
                aria-label="close"
                onClick={onClose}
                sx={{
                  position: "absolute",
                  right: 8,
                  top: 8,
                  color: (theme) => theme.palette.grey[500],
                }}
              >
                <CloseIcon />
              </IconButton>
            ) : null}
          </DialogTitle>
        );
      };

    const sendPuzzleToSave = () => {
        switch(puzzleName) {
            case "nonogram":
                if(selectedNonogram) {
                    NonogramService.saveNonogramToFile(fileName, selectedNonogram).then((response) => {
                        if(typeof(response.data) === "string") {
                            setResponseText(response.data)
                        }
                    }).catch((error) => {
                        console.log(error)
                    })
                }
                break
            case "sudoku":
                let sudokuToSave = mode === "READ" ? selectedSudoku : createdSudoku
                sudokuToSave.filled = calculateFilledFields(sudokuToSave.board)
                SudokuService.saveSudokuToFile(fileName, sudokuToSave).then((response) => {
                    if(typeof(response.data) === "string") {
                        setResponseText(response.data)
                    }
                }).catch()
                break
        }
        if(selectedNonogram) {
            
        }
    }

    const checkSaveFailed = (response: string) => {
        return response.startsWith("Save failed.")
    }

    const checkSaveSuccess = (response: string) => {
        return response.startsWith("Save success!")
    }

    return (
        <Dialog
            open={showSaveAlert}
            onClose={() => passShowSaveAlert(false)}
            aria-labelledby="alert-dialog-title"
            aria-describedby="alert-dialog-description"
        >
            <BootstrapDialogTitle id="customized-dialog-title" onClose={() => passShowSaveAlert(false)}>
                Save {puzzleName} - last step &nbsp;
            </BootstrapDialogTitle>
            <DialogContent>
                <DialogContentText id="alert-dialog-description" style={{display: "flex", flexDirection: "column"}}>
                    Enter file name to save {puzzleName}:
                    <TextField
                        onChange={(event: React.ChangeEvent<HTMLInputElement>) => inputChangeHandler(event)}
                        onKeyDown={(event: React.KeyboardEvent<HTMLDivElement>) => keyHandler(event)}
                        error={checkSaveFailed(responseText)}
                        helperText={checkSaveFailed(responseText) ? "File exists. Try other filename." : ""}
                    />
                </DialogContentText>
            </DialogContent>
            <DialogActions>
                <Button id="send-save-request" onClick={() => sendPuzzleToSave()} disabled={fileName.length < 3}>
                    SEND&nbsp;<FontAwesomeIcon icon={faRightLong}/>
                </Button>
            </DialogActions>
        </Dialog>
    )
}

export default connector(SaveModal)