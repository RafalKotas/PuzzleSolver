// redux
import { Dispatch } from "redux"
import { connect, ConnectedProps } from "react-redux"

// redux - store
import { AppState } from "../../store"

// mui
import { Checkbox, FormControlLabel, TextField } from "@mui/material"
import { useEffect, useState } from "react"

// styles
import "./StringPropTextField.css"

// other files
import { propDetails } from "./types"

interface OwnStringPropTextFieldProps {
    prop: propDetails,
    passValueToParent: (property: string, value: string, valid: boolean) => void
}

const mapStateToProps = (state: AppState) => ({
})

const mapDispatchToProps = (dispatch: Dispatch) => ({

})

const connector = connect(mapStateToProps, mapDispatchToProps)

type StringPropTextFieldPropsFromRedux = ConnectedProps<typeof connector>

type StringPropTextFieldProps = StringPropTextFieldPropsFromRedux & OwnStringPropTextFieldProps

const StringPropTextField : React.FC<StringPropTextFieldProps> = ({prop, passValueToParent}) => {

    let {label, defaultValue, helperText, required, notProvidedValue, minLength, maxLength, regex} = prop

    const propValidationSuccess = (text: string) => {
        return ((text.length >= minLength && text.length <= maxLength && regex.test(text)) || (!required && text === notProvidedValue))
    }

    const [value, setValue] = useState<string>(defaultValue)

    useEffect(() => {
        passValueToParent(prop.label, value, propValidationSuccess(value))
        //eslint-disable-next-line
    }, [value])

    const onInputChange = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>, prop: propDetails) => {
        let currentText = event.target.value
        setValue(currentText)
    }

    const onCheckChange = (event: React.ChangeEvent<HTMLInputElement>, checked: boolean) => {
        if(checked) {
            setValue(notProvidedValue)
        } else {
            setValue(defaultValue)
        }
    }

    return (
        <div className="puzzle-string-prop-tf">
            <TextField
                onChange={(event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => onInputChange(event, prop)}
                label={label.slice(0, 1).toUpperCase() + label.slice(1).toLocaleLowerCase()}
                helperText={!propValidationSuccess(value) ? helperText : ""}
                value={value}
                error={!propValidationSuccess(value)}
                margin="normal"
                variant="standard" 
            />
            {!required && 
                <FormControlLabel 
                    control={
                    <Checkbox 
                        onChange={(event: React.ChangeEvent<HTMLInputElement>, checked: boolean) => onCheckChange(event, checked)}
                    />} 
                    label="N/D" />
                }
        </div>
    )
}

export default connector(StringPropTextField)