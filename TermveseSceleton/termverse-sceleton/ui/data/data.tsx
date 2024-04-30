export interface StudySetProps {
    props: StudySetResponse
}

export interface StudySetPropsListProps {
    props: StudySetResponseList
}

export interface StudySetPropsList {
    list: StudySetDescription[]
}

export interface StudySetResponseList {
    list: StudySetResponse[]
}

export interface StudySetResponseListProps {
    props: StudySetResponseList
}

export interface StudySetDescription {
    id: number
    name: string
    pictureUrl: string | null
    created_at: Date
    size: number
}
export interface StudySetResponse {
    id: number
    name: string
    pictureUrl: string | null
    createdAt: string | null
}

export interface StudySetResponseList {
    list: StudySetResponse[]
}
export interface StudySetCreate {
    name: string
    pictureUrl: string | null
    terms: TermCreateDTO[]
}

export interface TermCreateDTO {
     term : string;
     definition : string;
     hint : string | null;
     picture_url : string | null;
     order : number;
}
export interface TermResponseDTO {
        id: number,
        term: string,
        definition: string,
        hint: string | null,
        picture_url: string | null,
        order: number
}

export interface UserProps {
    props: UserDescription
}
export interface UserDescription {
    id: number
    name: string
    profile_picture_url: string | null
}