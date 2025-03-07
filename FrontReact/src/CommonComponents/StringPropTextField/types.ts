export interface propDetails {
    label: string,
    minLength: number,
    maxLength: number,
    defaultValue: string,
    helperText: string,
    regex: RegExp,
    required: boolean,
    notProvidedValue: string,
    defaultValues: Array<string>
}