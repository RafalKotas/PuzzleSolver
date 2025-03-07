// redux
import { Dispatch } from "redux"
import { connect, ConnectedProps } from "react-redux"

// redux - store
import { AppState } from "../../store"

// mui
import { Autocomplete, Checkbox, FormControlLabel, TextField } from "@mui/material"
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
    const options = ["logi", "logiMix", "katana", "pazyl_pl"]

    let {label, defaultValue, helperText, required, notProvidedValue, minLength, maxLength, regex, defaultValues} = prop

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
        <div className="puzzle-string-prop">
            <div className="string-prop-tf">
                <Autocomplete
                    options={defaultValues}
                    freeSolo // Pozwala użytkownikowi wpisać własną wartość (możesz usunąć, jeśli chcesz ograniczyć do listy)
                    value={value}
                    onChange={(_, newValue) => onInputChange({ target: { value: newValue || "default" } } as React.ChangeEvent<HTMLInputElement>, prop)}
                    renderInput={(params) => (
                        <TextField
                            {...params}
                            label={label.charAt(0).toUpperCase() + label.slice(1).toLowerCase()}
                            helperText={!propValidationSuccess(value) ? helperText : ""}
                            error={!propValidationSuccess(value)}
                            margin="normal"
                            variant="standard"
                        />
                    )}
                />
            </div>
            <div className="string-prop-not-provided-value">
                {!required && 
                    <FormControlLabel 
                        control={
                        <Checkbox 
                            onChange={(event: React.ChangeEvent<HTMLInputElement>, checked: boolean) => onCheckChange(event, checked)}
                        />} 
                        label="N/D" 
                    />
                }
            </div>
        </div>
    )
}

export default connector(StringPropTextField)