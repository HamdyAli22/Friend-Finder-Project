import {WorkExperience} from './work-experience';
import {Interest} from './interest';
import {Language} from './language';

export class AboutProfile {
  id: number;
  personalInfo: string;
  address: string;
  workExperiences: WorkExperience[];
  interests: Interest[];
  languages: Language[];
}
