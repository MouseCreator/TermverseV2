import {StudySet} from "@/ui/components/study_set";

export interface StudySetProps {
    props: StudySetDescription
}

export interface StudySetPropsListProps {
    props: StudySetPropsList
}

export interface StudySetPropsList {
    list: StudySetDescription[]
}

export interface StudySetDescription {
    id: number
    name: string
    picture_url: string | null
    created_at: Date
    size: number
}

export interface UserProps {
    props: UserDescription
}
export interface UserDescription {
    id: number
    name: string
    profile_picture_url: string | null
}