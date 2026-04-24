// Icons.jsx — Lucide-style rounded line icons
// 2dp stroke, 24dp standard

const Icon = ({ d, size = 24, stroke = "currentColor", strokeWidth = 2, fill = "none", style = {}, ...p }) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill={fill} stroke={stroke}
    strokeWidth={strokeWidth} strokeLinecap="round" strokeLinejoin="round"
    style={style} {...p}>
    {typeof d === 'string' ? <path d={d} /> : d}
  </svg>
);

const IconSearch = (p) => <Icon {...p} d={<><circle cx="11" cy="11" r="7"/><path d="m20 20-3.5-3.5"/></>} />;
const IconCamera = (p) => <Icon {...p} d={<><path d="M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z"/><circle cx="12" cy="13" r="4"/></>} />;
const IconCameraSolid = (p) => <Icon {...p} fill="currentColor" stroke="none" d={<><path d="M20 7h-3.2l-1.5-2.2A2 2 0 0 0 13.7 4h-3.4a2 2 0 0 0-1.6.8L7.2 7H4a2 2 0 0 0-2 2v10a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2z"/><circle cx="12" cy="13.5" r="3.8" fill="#fff"/></>} />;
const IconSettings = (p) => <Icon {...p} d={<><circle cx="12" cy="12" r="3"/><path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 1 1-2.83 2.83l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 1 1-4 0v-.09a1.65 1.65 0 0 0-1-1.51 1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 1 1-2.83-2.83l.06-.06a1.65 1.65 0 0 0 .33-1.82 1.65 1.65 0 0 0-1.51-1H3a2 2 0 1 1 0-4h.09a1.65 1.65 0 0 0 1.51-1 1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 1 1 2.83-2.83l.06.06a1.65 1.65 0 0 0 1.82.33H9a1.65 1.65 0 0 0 1-1.51V3a2 2 0 1 1 4 0v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 1 1 2.83 2.83l-.06.06a1.65 1.65 0 0 0-.33 1.82V9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 1 1 0 4h-.09a1.65 1.65 0 0 0-1.51 1z"/></>} />;
const IconHome = (p) => <Icon {...p} d={<><path d="M3 9.5 12 3l9 6.5V20a2 2 0 0 1-2 2h-4v-7h-6v7H5a2 2 0 0 1-2-2z"/></>} />;
const IconFolder = (p) => <Icon {...p} d="M3 7a2 2 0 0 1 2-2h4l2 2h8a2 2 0 0 1 2 2v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z" />;
const IconFile = (p) => <Icon {...p} d={<><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><path d="M14 2v6h6"/></>} />;
const IconShare = (p) => <Icon {...p} d={<><circle cx="18" cy="5" r="3"/><circle cx="6" cy="12" r="3"/><circle cx="18" cy="19" r="3"/><path d="m8.6 13.5 6.8 4M15.4 6.5l-6.8 4"/></>} />;
const IconTrash = (p) => <Icon {...p} d={<><path d="M3 6h18"/><path d="M8 6V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6"/></>} />;
const IconEdit = (p) => <Icon {...p} d={<><path d="M17 3a2.828 2.828 0 1 1 4 4L7.5 20.5 2 22l1.5-5.5z"/></>} />;
const IconText = (p) => <Icon {...p} d={<><path d="M4 7V5h16v2"/><path d="M12 5v14"/><path d="M9 19h6"/></>} />;
const IconPlus = (p) => <Icon {...p} d="M12 5v14M5 12h14" />;
const IconArrowLeft = (p) => <Icon {...p} d={<><path d="m15 18-6-6 6-6"/></>} />;
const IconArrowRight = (p) => <Icon {...p} d={<><path d="m9 18 6-6-6-6"/></>} />;
const IconX = (p) => <Icon {...p} d="M18 6 6 18M6 6l12 12" />;
const IconCheck = (p) => <Icon {...p} d="m5 12 5 5L20 7" />;
const IconFlash = (p) => <Icon {...p} d="M13 2 3 14h9l-1 8 10-12h-9z" />;
const IconFlashOff = (p) => <Icon {...p} d={<><path d="M13 2 3 14h4m4 3-1 5 10-12h-4"/><path d="m2 2 20 20"/></>} />;
const IconImage = (p) => <Icon {...p} d={<><rect x="3" y="3" width="18" height="18" rx="2"/><circle cx="9" cy="9" r="1.5"/><path d="m21 15-5-5L5 21"/></>} />;
const IconRotate = (p) => <Icon {...p} d={<><path d="M3 12a9 9 0 1 0 2.64-6.36L3 8"/><path d="M3 3v5h5"/></>} />;
const IconCrop = (p) => <Icon {...p} d={<><path d="M6 2v14a2 2 0 0 0 2 2h14"/><path d="M18 22V8a2 2 0 0 0-2-2H2"/></>} />;
const IconWand = (p) => <Icon {...p} d={<><path d="m14 7 3 3M5 19l9-9"/><path d="M13 3v4M11 5h4M19 9v4M17 11h4"/></>} />;
const IconSparkle = (p) => <Icon {...p} d={<><path d="M12 3v4M12 17v4M3 12h4M17 12h4M5.6 5.6l2.8 2.8M15.6 15.6l2.8 2.8M5.6 18.4l2.8-2.8M15.6 8.4l2.8-2.8"/></>} />;
const IconChevronRight = (p) => <Icon {...p} d="m9 18 6-6-6-6" />;
const IconChevronDown = (p) => <Icon {...p} d="m6 9 6 6 6-6" />;
const IconDots = (p) => <Icon {...p} d={<><circle cx="5" cy="12" r="1" fill="currentColor"/><circle cx="12" cy="12" r="1" fill="currentColor"/><circle cx="19" cy="12" r="1" fill="currentColor"/></>} />;
const IconGrid = (p) => <Icon {...p} d={<><rect x="3" y="3" width="7" height="7" rx="1"/><rect x="14" y="3" width="7" height="7" rx="1"/><rect x="3" y="14" width="7" height="7" rx="1"/><rect x="14" y="14" width="7" height="7" rx="1"/></>} />;
const IconList = (p) => <Icon {...p} d={<><path d="M8 6h13M8 12h13M8 18h13"/><circle cx="4" cy="6" r="0.5" fill="currentColor"/><circle cx="4" cy="12" r="0.5" fill="currentColor"/><circle cx="4" cy="18" r="0.5" fill="currentColor"/></>} />;
const IconCopy = (p) => <Icon {...p} d={<><rect x="8" y="8" width="13" height="13" rx="2"/><path d="M16 8V6a2 2 0 0 0-2-2H5a2 2 0 0 0-2 2v9a2 2 0 0 0 2 2h2"/></>} />;
const IconCrown = (p) => <Icon {...p} d={<><path d="M3 7l4 4 5-7 5 7 4-4-2 12H5z"/></>} />;

Object.assign(window, {
  Icon, IconSearch, IconCamera, IconCameraSolid, IconSettings, IconHome, IconFolder, IconFile,
  IconShare, IconTrash, IconEdit, IconText, IconPlus, IconArrowLeft, IconArrowRight, IconX,
  IconCheck, IconFlash, IconFlashOff, IconImage, IconRotate, IconCrop, IconWand, IconSparkle,
  IconChevronRight, IconChevronDown, IconDots, IconGrid, IconList, IconCopy, IconCrown,
});
