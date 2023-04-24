// react
import React, { useEffect, useState } from "react"

// redux
import { connect, ConnectedProps } from "react-redux"
import { Dispatch } from "redux"

// redux - store
import { sudokuInformation, SetSelectedSudoku, SetCreatedSudoku, 
    CopySudokuToCreatedList, CopySudokuToSolverList } from "../../../../../../store/data/sudoku"
import { AppState } from "../../../../../../store"

// mui
import { Snackbar, Tooltip } from "@mui/material"
import MuiAlert, { AlertProps } from "@mui/material/Alert"

// fontawesome
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import { faCopy} from "@fortawesome/free-solid-svg-icons"
import { listIncludesFileName } from "../../../../../../store/data/sudoku/functions"



interface OwnCopySectionProps {
    sudokuDetails : sudokuInformation
}

const mapStateToProps = (state: AppState) => ({
    displayMode : state.displayReducer.displayMode,
    mode: state.displayReducer.mode,
    sudokusCreatedList: state.sudokuDataReducer.createdSudokusList,
    sudokusToReadList: state.sudokuDataReducer.sudokusList
})

const mapDispatchToProps = (dispatch: Dispatch) => ({
    setSelectedSudoku: (sudoku: sudokuInformation) => 
      dispatch(SetSelectedSudoku(sudoku)),
    setCreatedSudoku: (createdSudoku: sudokuInformation) =>
      dispatch(SetCreatedSudoku(createdSudoku)),
    copySudokuToCreatedList: (sudoku: sudokuInformation) =>
      dispatch(CopySudokuToCreatedList(sudoku)),
    copySudokuToSolverList: (sudoku: sudokuInformation) =>
      dispatch(CopySudokuToSolverList(sudoku))
})

const connector = connect(mapStateToProps, mapDispatchToProps)

type CopySectionPropsFromRedux = ConnectedProps<typeof connector>

type CopySectionProps = CopySectionPropsFromRedux & OwnCopySectionProps

const CopySection : React.FC<CopySectionProps> = ({mode, copySudokuToCreatedList, copySudokuToSolverList, sudokuDetails, 
    sudokusCreatedList, sudokusToReadList}) => {

    const [open, setOpen] = useState<boolean>(false)
    const [copySuccess, setCopySuccess] = useState<boolean | undefined>(undefined)

    const copySudoku = () => {
        if (mode === "READ") {
            if (listIncludesFileName(sudokusCreatedList, sudokuDetails)) {
                setCopySuccess(false)
            } else {
                copySudokuToCreatedList(sudokuDetails)
                setCopySuccess(true)
            }
        } else {
            if (listIncludesFileName(sudokusToReadList, sudokuDetails)) {
                setCopySuccess(false)
            } else {
                copySudokuToSolverList(sudokuDetails)
                setCopySuccess(true)
            }
        }
        if(copySuccess !== undefined) {
            setOpen(true)
        }
    }

    useEffect(() => {
        if(copySuccess !== undefined) {
            setOpen(true)
        }
    }, [copySuccess])

    const Alert = React.forwardRef<HTMLDivElement, AlertProps>(function Alert(
        props,
        ref,
    ) {
        return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />
    })

    const handleClose = (event: React.SyntheticEvent | Event, reason?: string) => {
        if (reason === "clickaway") {
            return
        }

        setOpen(false)
    }

    return (<section className={"copy-to-created-list"}>
        <div className="copy-icon-div" onClick={() => copySudoku()}>
            <Tooltip title={`Copy to ${mode === "READ" ? "created" : "read"} list!!!`} placement="top">
                <FontAwesomeIcon className={"copy-icon"} icon={faCopy} />
            </Tooltip>
        </div>
        <Snackbar
            open={open}
            autoHideDuration={2000}
            onClose={handleClose}
        >
            {
                (copySuccess && <Alert onClose={handleClose} severity="success" sx={{ width: "100%" }}>
                    {`Successfully copied ${sudokuDetails.label} to ${mode === "READ" ? "created" : "read"} list! - COPY SUCCESS :)`}
                </Alert>) || <Alert onClose={handleClose} severity="error" sx={{ width: "100%" }}>
                    {`File with given name is already in ${mode === "READ" ? "created" : "read"} list! - COPY FAIL :(`}
                </Alert>
            }
        </Snackbar>
    </section>)
}

export default connector(CopySection)