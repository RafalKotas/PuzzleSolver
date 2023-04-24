// react
import React, { useEffect, useState } from "react"

// (sub) components
import SaveButton from "../SaveButton/SaveButton"
import SaveModal from "../SaveModal/SaveModal"

// mui
import { Snackbar } from "@mui/material"
import MuiAlert, { AlertProps } from "@mui/material/Alert"

interface SaveSectionProps {
    disabledCondition: boolean
}

const SaveSection : React.FC<SaveSectionProps> = ({disabledCondition}) => {

    const [showSaveAlert, setShowSaveAlert] = useState<boolean>(false)
    const [open, setOpen] = useState<boolean>(false)
    const [savedFileName, setSavedFileName] = useState<string>("")

    useEffect(() => {
        if (savedFileName !== "") {
            setOpen(true)
        }
    }, [savedFileName])

    const getSavedFileName = (fileName: string) => {
        setSavedFileName(fileName)
    }

    const getShowSaveAlert = (show: boolean) => {
        setShowSaveAlert(show)
    }

    const handleClose = (event: React.SyntheticEvent | Event, reason?: string) => {
        if (reason === "clickaway") {
            return
        }

        setOpen(false)
    }

    const Alert = React.forwardRef<HTMLDivElement, AlertProps>(function Alert(
        props,
        ref,
    ) {
        return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />
    })

    return (
        <React.Fragment>
            <SaveButton disabled={disabledCondition} toggleAlert={() => setShowSaveAlert(true)} />
            <SaveModal
                showSaveAlert={showSaveAlert}
                passSavedFileName={getSavedFileName}
                passShowSaveAlert={getShowSaveAlert}
            />
            <Snackbar
                open={open}
                autoHideDuration={2000}
                onClose={handleClose}
            >
                <Alert onClose={handleClose} severity="success" sx={{ width: "100%" }}>
                    {"Nonogram saved successfuly in file " + savedFileName + ".json!"}
                </Alert>
            </Snackbar>
        </React.Fragment>
    )
}

export default SaveSection