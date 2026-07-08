export interface LeadType {
  id?: number;
  name: string;
  description?: string;
}

export interface CustomerLead {
  id?: number;
  customerName: string;
  mobile: string;
  alternateNumber?: string;
  email?: string;
  leadType?: LeadType;
  leadTypeId?: number;
  city?: string;
  address?: string;
  requirement?: string;
  leadSource?: string;
  assignedExecutive?: string;
  discussionDetails?: string;
  visitDate?: string;
  nextFollowupDate?: string;
  status?: string;
  priority?: string;
  createdDate?: string;
}

export interface FollowUp {
  id?: number;
  customerLeadId?: number;
  followupDate: string;
  remarks: string;
  statusAtFollowup?: string;
  nextFollowupDate?: string;
  createdDate?: string;
}

export interface Note {
  id?: number;
  customerLeadId?: number;
  noteText: string;
  createdDate?: string;
}

export interface DashboardStats {
  totalLeads: number;
  todaysFollowups: number;
  pendingFollowups: number;
  hotCustomers: number;
  closedDeals: number;
}

export interface LeadSearchCriteria {
  name?: string;
  mobile?: string;
  leadTypeId?: number;
  status?: string;
  priority?: string;
  city?: string;
  fromDate?: string;
  toDate?: string;
}

export const STATUS_OPTIONS = [
  'New', 'Contacted', 'Interested', 'Follow Up', 'Visit Scheduled',
  'Negotiation', 'Closed Won', 'Closed Lost', 'Not Interested'
];

export const PRIORITY_OPTIONS = ['Hot', 'Warm', 'Cold', 'Not a Customer'];
