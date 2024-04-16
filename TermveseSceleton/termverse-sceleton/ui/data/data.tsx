export interface StudySetProps {
    props: StudySetDescription
}
export interface StudySetDescription {
    id: number
    name: string
    picture_url: string | null
    created_at: Date
    size: number
}